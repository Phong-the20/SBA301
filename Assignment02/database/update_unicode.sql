USE A2TranThePhong_SE1910;
GO

UPDATE category SET category_name = N'Công nghệ', category_desciption = N'Tin tức công nghệ trong và ngoài nước' WHERE category_id = 1;
UPDATE category SET category_name = N'Giáo dục', category_desciption = N'Các hoạt động học tập, đào tạo tại Đại học FPT' WHERE category_id = 2;
UPDATE category SET category_name = N'Đời sống', category_desciption = N'Hoạt động ngoại khóa và văn hóa sinh viên' WHERE category_id = 3;
UPDATE category SET category_name = N'Lưu trữ', category_desciption = N'Danh mục cũ tạm ngưng hoạt động' WHERE category_id = 4;

UPDATE tag SET tag_name = N'AI & Robot', note = N'Chủ đề trí tuệ nhân tạo' WHERE tag_id = 1;
UPDATE tag SET tag_name = N'FPT University', note = N'Hoạt động nhà trường' WHERE tag_id = 2;
UPDATE tag SET tag_name = N'Sinh viên', note = N'Góc học tập & giải trí' WHERE tag_id = 3;

UPDATE news_article SET 
    news_title = N'Đại học FPT đẩy mạnh ứng dụng AI trong giảng dạy',
    headline = N'Xu hướng đào tạo công nghệ 2026',
    news_content = N'Nội dung chi tiết về các workshop và phòng lab AI mới phục vụ sinh viên...'
WHERE news_article_id = 1;

UPDATE news_article SET 
    news_title = N'Hội thảo khoa học công nghệ quốc tế năm 2026',
    headline = N'Gặp gỡ các chuyên gia đầu ngành',
    news_content = N'Hội thảo quy tụ hơn 50 diễn giả quốc tế chia sẻ về tương lai phần mềm...'
WHERE news_article_id = 2;

UPDATE news_article SET 
    news_title = N'Sinh viên khóa mới tự tin bước vào môi trường đại học',
    headline = N'Lễ khai giảng rực rỡ sắc cam',
    news_content = N'Hàng ngàn tân sinh viên tham gia ngày hội định hướng và các hoạt động sôi nổi...'
WHERE news_article_id = 3;
GO

