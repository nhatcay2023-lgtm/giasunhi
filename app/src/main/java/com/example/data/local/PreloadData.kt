package com.example.data.local

import com.example.data.model.*

object PreloadData {

    fun getInitialLessons(): List<LessonItem> = listOf(
        LessonItem(
            id = "math_1",
            subject = SubjectCategory.MATH,
            title = "Phép cộng trong phạm vi 10",
            description = "Học cách cộng các số nhỏ bằng hình ảnh quả táo và que tính",
            content = "Phép cộng là khi chúng mình gộp hai nhóm đồ vật lại với nhau để biết có tất cả bao nhiêu đồ vật.\n\nVí dụ: Tay trái bé cầm 3 quả táo đỏ 🍎🍎🍎, tay phải bé cầm thêm 2 quả táo xanh 🍏🍏. Gộp lại bé có: 3 + 2 = 5 quả táo!",
            visualAidText = "🍎🍎🍎 (3) + 🍏🍏 (2) = 🍎🍎🍎🍏🍏 (5)",
            sampleProblem = "Bạn Lan có 4 bông hoa hồng. Bạn Nam tặng Lan thêm 2 bông hoa vàng. Hỏi bạn Lan có tất cả bao nhiêu bông hoa?",
            stepByStepSolution = "Bước 1: Tóm tắt bài toán:\n- Lan có: 4 bông hoa\n- Nam tặng thêm: 2 bông hoa\n\nBước 2: Phép tính:\n4 + 2 = 6 (bông hoa)\n\nBước 3: Đáp số: 6 bông hoa. Bé giỏi lắm!",
            isCompleted = false,
            starsReward = 5
        ),
        LessonItem(
            id = "math_2",
            subject = SubjectCategory.MATH,
            title = "Phép trừ trong phạm vi 10",
            description = "Bớt đi, cho đi hoặc vỡ mất đồ vật thì làm phép trừ",
            content = "Phép trừ dùng dấu trừ (-). Khi có một số đồ vật và bớt đi một phần, số lượng còn lại sẽ ít hơn.\n\nVí dụ: Trên cành cây có 6 chú chim 🐦🐦🐦🐦🐦🐦. Bỗng 2 chú chim bay đi ✈️. Trên cành còn lại: 6 - 2 = 4 chú chim.",
            visualAidText = "🎈🎈🎈🎈🎈🎈 (6 bóng) - 🎈🎈 (bớt 2) = 🎈🎈🎈🎈 (4 bóng)",
            sampleProblem = "Bé có 7 viên kẹo sô-cô-la 🍬. Bé chia cho em ngoan 3 viên. Hỏi bé còn lại mấy viên kẹo?",
            stepByStepSolution = "Bước 1: Ban đầu có 7 viên kẹo.\nBước 2: Cho em 3 viên nghĩa là làm phép tính trừ:\n7 - 3 = 4 (viên kẹo)\n\nBước 3: Đáp số: 4 viên kẹo. Bé rất biết yêu thương em!",
            isCompleted = false,
            starsReward = 5
        ),
        LessonItem(
            id = "math_3",
            subject = SubjectCategory.MATH,
            title = "So sánh Lớn hơn (>), Bé hơn (<), Bằng nhau (=)",
            description = "Cá sấu đói bụng luôn mở to miệng về phía bên có nhiều thức ăn hơn!",
            content = "Mẹo ghi nhớ siêu dễ cho bé lớp 1:\nDấu lớn hơn (>) và dấu bé hơn (<) giống như miệng của chú Cá Sấu 🐊. Chú cá sấu luôn há miệng thật to về phía số lớn hơn để ăn thật nhiều thức ăn!\nNếu hai bên bằng nhau, ta dùng hai vạch ngang bằng nhau (=).",
            visualAidText = "🐟🐟🐟🐟 (4) > 🐟🐟 (2) | 🐊 há miệng sang số 4",
            sampleProblem = "Điền dấu thích hợp (<, >, =) vào chỗ chấm: 8 ... 5",
            stepByStepSolution = "Bước 1: Đếm xem 8 hay 5 nhiều hơn.\nSố 8 lớn hơn số 5 vì 8 que tính nhiều hơn 5 que tính.\n\nBước 2: Chú cá sấu sẽ há miệng quay về số 8: 8 > 5.\n\nĐáp án: Dấu >",
            isCompleted = false,
            starsReward = 5
        ),
        LessonItem(
            id = "math_4",
            subject = SubjectCategory.MATH,
            title = "Hình tròn, Hình vuông, Hình tam giác, Hình chữ nhật",
            description = "Nhận biết các hình học kỳ diệu quanh em",
            content = "Xung quanh bé có rất nhiều hình dạng xinh đẹp:\n- Hình tròn 🟡: tròn xoe như ông mặt trời hay chiếc bánh quy tròn.\n- Hình vuông 🟥: có 4 cạnh bằng nhau như chiếc bánh chưng ngày Tết.\n- Hình tam giác 🔺: có 3 góc nhọn như mái nhà ngói đỏ.\n- Hình chữ nhật 🟧: có 2 cạnh dài và 2 cạnh ngắn như chiếc bảng lớp học hay bìa quyển sách!",
            visualAidText = "🟡 Tròn | 🟥 Vuông | 🔺 Tam giác | 🟧 Chữ nhật",
            sampleProblem = "Đồng hồ treo tường tròn xoe ở phòng khách là hình gì nhỉ bé?",
            stepByStepSolution = "Bước 1: Quan sát đồng hồ thấy không có góc nhọn, tròn vo.\nBước 2: Đó chính là Hình Tròn 🟡!\nBé quan sát rất tinh mắt!",
            isCompleted = false,
            starsReward = 5
        ),
        LessonItem(
            id = "viet_1",
            subject = SubjectCategory.VIETNAMESE,
            title = "5 Dấu Thanh Kỳ Diệu trong Tiếng Việt",
            description = "Học thanh Huyền, Sắc, Hỏi, Ngã, Nặng cùng các con vật",
            content = "Tiếng Việt có 5 dấu thanh làm cho giọng nói du dương như nốt nhạc:\n1. Thanh sắc (´): bay vút lên cao như chú chim sẻ.\n2. Thanh huyền (`): êm dịu xuôi xuống như chiếc lá rơi.\n3. Thanh hỏi (?): uốn cong cong như chiếc móc câu cá.\n4. Thanh ngã (~): lượn sóng như sóng biển nhấp nhô.\n5. Thanh nặng (.): giọt nước đọng dưới đáy chữ.",
            visualAidText = "ba (không dấu) -> bà (huyền) -> bá (sắc) -> bả (hỏi) -> bã (ngã) -> bạ (nặng)",
            sampleProblem = "Từ 'Mẹ' trong 'Bé yêu Mẹ' có dấu thanh gì?",
            stepByStepSolution = "Bước 1: Chữ Mẹ có dấu chấm ở phía dưới chữ e.\nBước 2: Dấu chấm ở dưới gọi là Dấu Nặng (.).\nĐáp án: Dấu Nặng. Bé phát âm thật to: 'Mẹ ơi!'",
            isCompleted = false,
            starsReward = 5
        ),
        LessonItem(
            id = "viet_2",
            subject = SubjectCategory.VIETNAMESE,
            title = "Ghép vần vui: an, at, am, ap",
            description = "Ghép chữ cái a với n, t, m, p để tạo thành các vần hay",
            content = "Bé hãy cùng ghép nào:\n- a + n = an (cái bàn, hoa ban, đàn gà)\n- a + t = at (bát cơm, bãi cát, hát ca)\n- a + m = am (quả cam, con tằm, chăm chỉ)\n- a + p = ap (xe đạp, tháp rùa, cặp sách)",
            visualAidText = "a + n = an 🐥 | a + t = at 🥣 | a + m = am 🍊",
            sampleProblem = "Điền vần 'an' hay 'at' vào chỗ trống: Chú chim h_ _ líu lo trên cành.",
            stepByStepSolution = "Bước 1: Chim líu lo thì đang làm gì? Chim đang Hát!\nBước 2: Chữ H ghép với vần 'at' và dấu sắc: H + at + sắc = Hát.\nĐáp án: Điền vần 'at'.",
            isCompleted = false,
            starsReward = 5
        ),
        LessonItem(
            id = "science_1",
            subject = SubjectCategory.SCIENCE,
            title = "Năm Giác Quan Của Bé",
            description = "Khám phá xem Mắt, Tai, Mũi, Lưỡi, Da giúp bé như thế nào nhé!",
            content = "Cơ thể bé thật kỳ diệu với 5 giác quan:\n1. Đôi mắt 👀 (Thị giác): Giúp bé ngắm nhìn thế giới đầy màu sắc.\n2. Đôi tai 👂 (Thính giác): Giúp bé nghe tiếng chim hót và lời ru của mẹ.\n3. Cái mũi 👃 (Khứu giác): Ngửi mùi thơm của hoa và thức ăn ngon.\n4. Cái lưỡi 👅 (Vị giác): Nếm vị ngọt của kem, chua của chanh.\n5. Bàn tay & Da ✋ (Xúc giác): Cảm nhận ấm áp và mềm mại.",
            visualAidText = "👀 Nhìn | 👂 Nghe | 👃 Ngửi | 👅 Nếm | ✋ Chạm",
            sampleProblem = "Bé dùng giác quan nào để nghe cô giáo giảng bài trên lớp?",
            stepByStepSolution = "Đáp án: Đôi tai (Thính giác) xinh xắn giúp bé lắng nghe những bài học bổ ích từ thầy cô!",
            isCompleted = false,
            starsReward = 5
        ),
        LessonItem(
            id = "riddle_1",
            subject = SubjectCategory.BRAIN_TEASER,
            title = "Đố Vui Động Vật Thông Minh",
            description = "Rèn luyện tư duy quan sát và sự nhanh trí cho bé",
            content = "Câu đố giúp não bé tập thể dục mỗi ngày, giúp bé phản xạ nhanh và hiểu biết sâu rộng hơn về thế giới tự nhiên xung quanh!",
            visualAidText = "🐰 🐶 🐱 🐘 Các bạn thú đáng yêu đang đố bé nè!",
            sampleProblem = "Con gì đuôi ngắn tai dài, mắt hồng lông mượt, có tài nhảy nhanh?",
            stepByStepSolution = "Gợi ý: Bạn này rất thích ăn củ cà rốt 🥕 màu cam giòn ngọt!\nĐáp án: Đó là Bạn Thỏ Trắng 🐰 xinh xắn!",
            isCompleted = false,
            starsReward = 5
        )
    )

    fun getInitialQuestions(): List<QuizQuestion> = listOf(
        QuizQuestion(
            id = "q1",
            subject = SubjectCategory.MATH,
            questionText = "Phép tính nào dưới đây có kết quả bằng 7?",
            visualAid = "🍎🍎🍎 + 🍎🍎🍎🍎 = ?",
            options = listOf("3 + 4", "2 + 4", "5 + 3", "6 + 2"),
            correctIndex = 0,
            explanation = "3 cộng 4 bằng 7. Bé đếm: 3 ngón tay thêm 4 ngón tay là 7 ngón tay!",
            difficulty = 1
        ),
        QuizQuestion(
            id = "q2",
            subject = SubjectCategory.MATH,
            questionText = "Mẹ mua 9 quả cam 🍊. Cả nhà cùng ăn hết 4 quả. Hỏi còn lại mấy quả cam?",
            visualAid = "🍊🍊🍊🍊🍊🍊🍊🍊🍊 (9) - 4 quả ăn hết",
            options = listOf("3 quả", "4 quả", "5 quả", "6 quả"),
            correctIndex = 2,
            explanation = "Làm phép trừ: 9 - 4 = 5 quả cam. Bé tính nhẩm rất xuất sắc!",
            difficulty = 1
        ),
        QuizQuestion(
            id = "q3",
            subject = SubjectCategory.MATH,
            questionText = "Bé hãy chọn dấu thích hợp điền vào chỗ chấm: 6 ... 9",
            visualAid = "🐟 🐟 🐟 🐟 🐟 🐟 [ ? ] 🐟 🐟 🐟 🐟 🐟 🐟 🐟 🐟 🐟",
            options = listOf(">", "<", "=", "+"),
            correctIndex = 1,
            explanation = "Số 6 bé hơn số 9 (6 < 9). Miệng chú cá sấu há to về phía số 9!",
            difficulty = 1
        ),
        QuizQuestion(
            id = "q4",
            subject = SubjectCategory.MATH,
            questionText = "Hình nào dưới đây có 3 góc nhọn và 3 cạnh thẳng?",
            visualAid = "🟡  |  🔺  |  🟥  |  🟧",
            options = listOf("Hình tròn", "Hình vuông", "Hình tam giác", "Hình chữ nhật"),
            correctIndex = 2,
            explanation = "Hình tam giác có đúng 3 cạnh và 3 đỉnh nhọn giống chiếc nón lá!",
            difficulty = 2
        ),
        QuizQuestion(
            id = "q5",
            subject = SubjectCategory.VIETNAMESE,
            questionText = "Từ nào dưới đây chứa vần 'an'?",
            visualAid = "📖 Đi tìm vần 'an' cùng Cô Cú",
            options = listOf("Cái bàn", "Bát cơm", "Hạt thóc", "Mặt trời"),
            correctIndex = 0,
            explanation = "Từ 'bàn' gồm âm b + vần an + dấu huyền: b-an-ban-huyền-bàn!",
            difficulty = 1
        ),
        QuizQuestion(
            id = "q6",
            subject = SubjectCategory.VIETNAMESE,
            questionText = "Dấu thanh trong từ 'Cá' (trong con cá 🐟) là dấu gì?",
            visualAid = "Cá bơi tung tăng",
            options = listOf("Dấu Huyền", "Dấu Sắc", "Dấu Hỏi", "Dấu Nặng"),
            correctIndex = 1,
            explanation = "Chữ 'Cá' có dấu sắc (´) vút lên cao!",
            difficulty = 1
        ),
        QuizQuestion(
            id = "q7",
            subject = SubjectCategory.SCIENCE,
            questionText = "Trước khi ăn cơm và sau khi đi vệ sinh, bé cần làm gì để bảo vệ sức khỏe?",
            visualAid = "🧼 💧 Rửa tay sạch sẽ",
            options = listOf("Đi xem ti vi", "Rửa tay sạch bằng xà phòng", "Chạy nhảy nô đùa", "Uống nước ngọt"),
            correctIndex = 1,
            explanation = "Rửa tay bằng xà phòng giúp tiêu diệt vi khuẩn, giữ cho bé luôn khỏe mạnh không bị đau bụng!",
            difficulty = 1
        ),
        QuizQuestion(
            id = "q8",
            subject = SubjectCategory.BRAIN_TEASER,
            questionText = "Tìm số thích hợp điền vào dấu ?: 2, 4, 6, [ ? ], 10",
            visualAid = "Đếm cách 2 số: 2 -> 4 -> 6 -> ?",
            options = listOf("7", "8", "9", "5"),
            correctIndex = 1,
            explanation = "Mỗi số sau hơn số trước 2 đơn vị: 6 + 2 = 8. Bé rất thông minh!",
            difficulty = 2
        )
    )

    fun getInitialBadges(): List<BadgeItem> = listOf(
        BadgeItem("b1", "Bé Ngoan Nhập Học", "Bắt đầu hành trình học tập cùng Gia sư AI", "🎒", true, "Hôm nay", 0),
        BadgeItem("b2", "Vua Tính Nhẩm", "Đạt 10 phép tính cộng trừ chính xác", "👑", false, null, 15),
        BadgeItem("b3", "Nhà Thơ Nhí", "Hoàn thành 5 bài học Tiếng Việt và ghép vần", "✍️", false, null, 25),
        BadgeItem("b4", "Nhà Thám Hiểm", "Trả lời đúng 5 câu hỏi Tự nhiên & Xã hội", "🌱", false, null, 35),
        BadgeItem("b5", "Trí Tuệ Ngôi Sao", "Tích lũy được 50 ngôi sao vàng", "⭐", false, null, 50),
        BadgeItem("b6", "Cú Mèo Siêu Cấp", "Hoàn thành bài kiểm tra định kỳ 10/10 điểm", "🦉", false, null, 80)
    )

    fun getInitialParentPosts(): List<ParentCommunityPost> = listOf(
        ParentCommunityPost(
            id = "post_1",
            author = "Cô Mai Lan",
            authorRole = "Giáo viên tiểu học (12 năm kinh nghiệm)",
            title = "Bí quyết giúp con thích học Toán đố lớp 1 mà không sợ hãi",
            content = "Nhiều phụ huynh chia sẻ rằng con rất nhanh khi tính nhẩm nhưng gặp bài toán có lời văn là 'tịt'. Lý do là bé chưa tưởng tượng được tình huống. Bố mẹ hãy dùng que tính, viên sỏi hoặc chính những món đồ chơi con thích để mô phỏng: 'Con có 3 cái xe ô tô, mẹ cho con thêm 2 cái, con thấy nhiều lên hay ít đi?'. Khi hình tượng hóa được, con sẽ tự tin giải quyết trong nháy mắt!",
            category = "Toán đố vui",
            likesCount = 38,
            commentsCount = 12,
            isSaved = true,
            date = "Hôm nay"
        ),
        ParentCommunityPost(
            id = "post_2",
            author = "Bác sĩ Nhi khoa Tuấn",
            authorRole = "Khoa Tâm lý & Phát triển Trẻ nhỏ",
            title = "Thời gian sử dụng thiết bị học tập hợp lý cho trẻ 6 tuổi",
            content = "Đối với trẻ lớp 1, thời gian tập trung tối ưu là khoảng 15-20 phút mỗi phiên học. Sau đó nên cho bé nghỉ ngơi mắt 5 phút, uống nước hoặc vận động nhẹ. Ứng dụng gia sư AI tương tác bằng giọng nói là giải pháp tuyệt vời vì bé không phải nhìn chằm chằm vào màn hình mà có thể vừa nghe vừa phản hồi tự nhiên.",
            category = "Kỷ luật tích cực",
            likesCount = 45,
            commentsCount = 8,
            isSaved = false,
            date = "Hôm qua"
        ),
        ParentCommunityPost(
            id = "post_3",
            author = "Mẹ Bống (Thanh Hóa)",
            authorRole = "Phụ huynh có 2 con vào lớp 1",
            title = "Mẹo rèn thói quen ngồi vào bàn học đúng giờ mỗi tối",
            content = "Gia đình mình đặt chuông đồng hồ 'Giờ Vàng Học Cùng Cú Mèo' vào lúc 19:30 mỗi tối. Trước khi học, hai mẹ con cùng chuẩn bị bàn học gọn gàng. Khi có bạn Cú Mèo AI trò chuyện và khen ngợi bé, con rất hào hứng và tự giác mở ứng dụng mà không cần mẹ phải nhắc nhở hay giục giã!",
            category = "Rèn chữ & Đọc",
            likesCount = 29,
            commentsCount = 6,
            isSaved = false,
            date = "2 ngày trước"
        )
    )
}
