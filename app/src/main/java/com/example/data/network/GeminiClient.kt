package com.example.data.network

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private const val SYSTEM_INSTRUCTION = """
Bạn là "Cô Cú Mèo Thông Thái" (hoặc Bạn Cú Vui Vẻ) - một gia sư AI siêu dễ thương, ngọt ngào và kiên nhẫn dành riêng cho các bé học sinh lớp 1 tiểu học (khoảng 6 tuổi) tại Việt Nam.

Quy tắc ứng xử và phong cách giảng dạy:
1. Luôn xưng hô thân mật: "Cô Cú" (hoặc "Bạn Cú") và gọi người dùng là "Bé ngoan", "Bé yêu" hoặc "Bạn nhỏ".
2. Giọng điệu luôn hào hứng, ấm áp, tích cực và tràn ngập lời khen ngợi (ví dụ: "Hoan hô bé yêu!", "Bé tính toán siêu quá!", "Tuyệt vời lắm!").
3. Giải thích khái niệm hoặc bài toán lớp 1 theo 3 bước cực kỳ đơn giản, ngắn gọn:
   - Bước 1: Hình dung vui (dùng emoji quen thuộc: quả táo 🍎, kẹo 🍬, que tính 🥢, chú gà con 🐥, ngôi sao ⭐).
   - Bước 2: Phép tính hoặc quy tắc kỳ diệu (viết to, rõ ràng: ví dụ 3 + 2 = 5).
   - Bước 3: Lời kết và câu đố nhỏ hoặc lời khen cho bé.
4. Với các bài toán đố có lời văn: Hãy tóm tắt lại bài toán bằng 2 câu thật ngắn, giúp bé xác định là "nhiều hơn/thêm vào" (làm phép cộng) hay "bớt đi/cho đi/bay mất" (làm phép trừ).
5. Tuyệt đối an toàn cho trẻ em: Không nói nội dung bạo lực, người lớn, không dùng thuật ngữ cao siêu khó hiểu. Mỗi câu trả lời ngắn gọn (khoảng 3 đến 5 đoạn ngắn dễ đọc, kèm emoji sinh động).
"""

    suspend fun askTutor(
        userMessage: String,
        conversationHistory: List<Pair<String, String>> = emptyList(),
        preferredModel: String = "gemini-3.5-flash"
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        // If API key is empty or default placeholder, or if network fails, we provide smart grade 1 offline response
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "Gemini API key is not configured or is placeholder. Using smart offline tutor logic.")
            return@withContext Result.success(generateSmartOfflineTutorResponse(userMessage))
        }

        try {
            // Build Gemini generateContent payload
            val rootJson = JSONObject()

            // System instruction
            val systemContent = JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().put("text", SYSTEM_INSTRUCTION)))
            }
            rootJson.put("systemInstruction", systemContent)

            // Contents array (multi-turn conversation)
            val contentsArray = JSONArray()

            // Add recent history turns (limit to last 4 for efficiency)
            val recentHistory = conversationHistory.takeLast(4)
            for ((role, text) in recentHistory) {
                val turnRole = if (role == "user") "user" else "model"
                val turnContent = JSONObject().apply {
                    put("role", turnRole)
                    put("parts", JSONArray().put(JSONObject().put("text", text)))
                }
                contentsArray.put(turnContent)
            }

            // Current prompt
            val currentTurn = JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", userMessage)))
            }
            contentsArray.put(currentTurn)

            rootJson.put("contents", contentsArray)

            // Generation config
            val genConfig = JSONObject().apply {
                put("temperature", 0.7)
                put("topP", 0.95)
                put("maxOutputTokens", 1024)
            }
            rootJson.put("generationConfig", genConfig)

            val requestBody = rootJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

            // Request url
            val url = "$BASE_URL$preferredModel:generateContent?key=$apiKey"
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val contentObj = candidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val replyText = parts.getJSONObject(0).optString("text")
                        if (replyText.isNotBlank()) {
                            return@withContext Result.success(replyText)
                        }
                    }
                }
            }

            Log.w(TAG, "Gemini API returned error code ${response.code}: $responseBody. Falling back to offline tutor.")
            Result.success(generateSmartOfflineTutorResponse(userMessage))
        } catch (e: Exception) {
            Log.e(TAG, "Network call failed, falling back to smart offline tutor", e)
            Result.success(generateSmartOfflineTutorResponse(userMessage))
        }
    }

    /**
     * Smart built-in offline educational tutor engine:
     * Parses grade 1 math questions (e.g. "5 + 3 = ?", "8 - 4", "Lan có 4 quả táo..."),
     * comparisons, letters, riddles and gives cheerful, accurate step-by-step explanations.
     */
    fun generateSmartOfflineTutorResponse(query: String): String {
        val q = query.trim().lowercase()

        // 1. Math addition parsing (e.g. "3 + 4", "5 cộng 2")
        val addRegex = Regex("""(\d{1,2})\s*(\+|\bcộng\b|\bvới\b)\s*(\d{1,2})""")
        val addMatch = addRegex.find(q)
        if (addMatch != null) {
            val a = addMatch.groupValues[1].toIntOrNull() ?: 0
            val b = addMatch.groupValues[3].toIntOrNull() ?: 0
            val sum = a + b
            val applesA = "🍎".repeat(minOf(a, 10))
            val applesB = "🍏".repeat(minOf(b, 10))
            return """
🦉 Cô Cú giải đáp cho bé yêu nè!

🍎 Bước 1: Chúng mình cùng đếm nhé:
Bé có $a quả táo đỏ: $applesA
Bé được bạn tặng thêm $b quả táo xanh: $applesB

✨ Bước 2: Phép tính kỳ diệu:
$a + $b = $sum

🎉 Bước 3: Đáp số là $sum!
Hoan hô bé yêu! Bé tính nhẩm siêu giỏi, cô Cú tặng bé 1 ngôi sao vàng ⭐ nhé!
""".trimIndent()
        }

        // 2. Math subtraction parsing (e.g. "9 - 4", "8 trừ 3")
        val subRegex = Regex("""(\d{1,2})\s*(-|\btrừ\b|\bbớt\b)\s*(\d{1,2})""")
        val subMatch = subRegex.find(q)
        if (subMatch != null) {
            val a = subMatch.groupValues[1].toIntOrNull() ?: 0
            val b = subMatch.groupValues[3].toIntOrNull() ?: 0
            val diff = a - b
            val candyA = "🍬".repeat(minOf(a, 10))
            return """
🦉 Cô Cú giúp bé giải phép trừ siêu nhanh nè!

🍬 Bước 1: Lúc đầu bé có $a viên kẹo ngọt:
$candyA

✨ Bước 2: Cho bạn ngoan $b viên kẹo, tức là bớt đi $b:
$a - $b = $diff

🎉 Bước 3: Bé còn lại đúng $diff viên kẹo!
Bé rất thông minh và biết chia sẻ! Cô Cú vỗ tay khen bé yêu 👏⭐!
""".trimIndent()
        }

        // 3. Math Word Problems (Toán có lời văn)
        if (q.contains("quả") || q.contains("kẹo") || q.contains("hoa") || q.contains("bông") || q.contains("con chim") || q.contains("bạn")) {
            return """
🦉 Bài toán đố này rất thú vị bé ơi!

📖 Tóm tắt bài toán:
- Lúc đầu chúng mình có số lượng ban đầu.
- Khi có thêm, tặng thêm hoặc gộp lại -> Chúng mình làm PHÉP CỘNG (+).
- Khi bớt đi, bay đi, cho bạn hoặc ăn mất -> Chúng mình làm PHÉP TRỪ (-).

💡 Mẹo của Cô Cú:
Bé hãy đếm xem lúc đầu có mấy món đồ, rồi thêm vào hay bớt đi nhé!
Nếu bé có số cụ thể (ví dụ: "Có 5 quả táo, ăn 2 quả còn mấy?"), bé cứ bấm Micro 🎙️ nói cho cô Cú nghe nhé!
""".trimIndent()
        }

        // 4. Vietnamese alphabet & spelling
        if (q.contains("chữ") || q.contains("vần") || q.contains("dấu") || q.contains("tiếng việt") || q.contains("đọc")) {
            return """
🦉 Ôi Tiếng Việt của chúng mình hay tuyệt vời bé yêu ơi!

🎵 Mẹo ghi nhớ 5 dấu thanh của Cô Cú:
- Dấu Sắc (´): Vút lên cao như chú chim bay 🕊️
- Dấu Huyền (`): Trầm êm như chiếc lá rơi 🍃
- Dấu Hỏi (?): Uốn cong như chiếc móc câu cá 🎣
- Dấu Ngã (~): Lượn sóng nhấp nhô trên biển 🌊
- Dấu Nặng (.): Giọt ngọc đọng lại xinh xắn 💧

Bé muốn ghép vần chữ gì nào? Bé nói cho Cô Cú nghe để cô hướng dẫn nhé! ⭐
""".trimIndent()
        }

        // 5. Riddles (Đố vui)
        if (q.contains("đố") || q.contains("câu đố") || q.contains("con gì")) {
            return """
🦉 Ha ha, Cô Cú có một câu đố vui dành tặng bé thông minh nè:

❓ "Con gì mào đỏ gáy ò ó o,
Sáng sớm gọi cả nhà cùng thức giấc?"

👉 Bé thử đoán xem là bạn nào nhé?
(Gợi ý nhỏ: Bạn ấy có bộ lông rất sặc sỡ và thích ăn thóc đấy! 🐥)
""".trimIndent()
        }

        // Default friendly response
        return """
🦉 Cô Cú Mèo chào bé ngoan!
Câu hỏi của bé rất hay và đáng yêu! Cô Cú luôn ở đây cùng bé học Toán tư duy 🔢, Tiếng Việt 📖, và khám phá thế giới tự nhiên 🌱.

Bé có thể hỏi cô:
- Các phép tính: ví dụ "4 + 5 bằng mấy cô ơi?"
- Bài toán đố: "Có 6 quả bóng, nổ 2 quả còn mấy?"
- Hoặc đố vui: "Đố cô một câu đố vui!"

Bé hãy bấm vào nút Micro 🎙️ để nói chuyện với Cô Cú nhé! ✨⭐
""".trimIndent()
    }
}
