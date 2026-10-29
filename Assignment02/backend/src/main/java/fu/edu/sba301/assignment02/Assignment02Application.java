package fu.edu.sba301.assignment02;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.entity.Category;
import fu.edu.sba301.assignment02.entity.NewsArticle;
import fu.edu.sba301.assignment02.entity.SystemAccount;
import fu.edu.sba301.assignment02.entity.Tag;
import fu.edu.sba301.assignment02.repository.CategoryRepository;
import fu.edu.sba301.assignment02.repository.NewsArticleRepository;
import fu.edu.sba301.assignment02.repository.SystemAccountRepository;
import fu.edu.sba301.assignment02.repository.TagRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class Assignment02Application {

    public static void main(String[] args) {
        SpringApplication.run(Assignment02Application.class, args);
    }

    @Bean
    public CommandLineRunner seedDatabase(
        SystemAccountRepository accountRepository,
        CategoryRepository categoryRepository,
        TagRepository tagRepository,
        NewsArticleRepository newsRepository
    ) {
        return args -> {
            if (accountRepository.count() == 0) {
                // 1. Seed Accounts
                SystemAccount admin = accountRepository.save(new SystemAccount(
                    null, "Super Admin", "admin@news.com", 1, "admin123"
                ));
                SystemAccount staffPhong = accountRepository.save(new SystemAccount(
                    null, "Tran The Phong (Staff)", "phongtt@news.com", 2, "staff123"
                ));
                SystemAccount staffDemo = accountRepository.save(new SystemAccount(
                    null, "Staff Demo", "staff@news.com", 2, "staff123"
                ));

                // 2. Seed Categories
                Category catTech = categoryRepository.save(new Category(
                    null, "Công nghệ", "Tin tức công nghệ trong và ngoài nước", 1
                ));
                Category catEdu = categoryRepository.save(new Category(
                    null, "Giáo dục", "Các hoạt động học tập, đào tạo tại Đại học FPT", 1
                ));
                Category catLife = categoryRepository.save(new Category(
                    null, "Đời sống", "Hoạt động ngoại khóa và văn hóa sinh viên", 1
                ));
                Category catArchive = categoryRepository.save(new Category(
                    null, "Lưu trữ", "Danh mục cũ tạm ngưng hoạt động", 0
                ));

                // 3. Seed Tags
                Tag tagAI = tagRepository.save(new Tag(null, "AI & Robot", "Chủ đề trí tuệ nhân tạo"));
                Tag tagFPT = tagRepository.save(new Tag(null, "FPT University", "Hoạt động nhà trường"));
                Tag tagStudent = tagRepository.save(new Tag(null, "Sinh viên", "Góc học tập & giải trí"));

                // 4. Seed News
                NewsArticle news1 = new NewsArticle();
                news1.setNewsTitle("Đại học FPT đẩy mạnh ứng dụng AI trong giảng dạy");
                news1.setHeadline("Xu hướng đào tạo công nghệ 2026");
                news1.setNewsContent("Nội dung chi tiết về các workshop và phòng lab AI mới phục vụ sinh viên...");
                news1.setNewsSource("FPT News");
                news1.setCategory(catTech);
                news1.setNewsStatus(1);
                news1.setCreatedBy(staffPhong);
                news1.setCreatedDate(LocalDateTime.now().minusDays(2));
                news1.setModifiedDate(LocalDateTime.now().minusDays(2));
                news1.setTags(Arrays.asList(tagAI, tagFPT));
                newsRepository.save(news1);

                NewsArticle news2 = new NewsArticle();
                news2.setNewsTitle("Hội thảo khoa học công nghệ quốc tế năm 2026");
                news2.setHeadline("Gặp gỡ các chuyên gia đầu ngành");
                news2.setNewsContent("Hội thảo quy tụ hơn 50 diễn giả quốc tế chia sẻ về tương lai phần mềm...");
                news2.setNewsSource("Tech Daily");
                news2.setCategory(catTech);
                news2.setNewsStatus(1);
                news2.setCreatedBy(staffPhong);
                news2.setCreatedDate(LocalDateTime.now().minusDays(1));
                news2.setModifiedDate(LocalDateTime.now().minusDays(1));
                news2.setTags(Arrays.asList(tagAI));
                newsRepository.save(news2);

                NewsArticle news3 = new NewsArticle();
                news3.setNewsTitle("Sinh viên khóa mới tự tin bước vào môi trường đại học");
                news3.setHeadline("Lễ khai giảng rực rỡ sắc cam");
                news3.setNewsContent("Hàng ngàn tân sinh viên tham gia ngày hội định hướng đầy hào hứng...");
                news3.setNewsSource("Campus Life");
                news3.setCategory(catEdu);
                news3.setNewsStatus(1);
                news3.setCreatedBy(staffDemo);
                news3.setCreatedDate(LocalDateTime.now());
                news3.setModifiedDate(LocalDateTime.now());
                news3.setTags(Arrays.asList(tagFPT, tagStudent));
                newsRepository.save(news3);

                System.out.println(">>> [Assignment 02] Sample data successfully seeded into database! <<<");
            }
        };
    }
}

