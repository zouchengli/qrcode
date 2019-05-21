package site.clzblog.qrcode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.clzblog.qrcode.utils.QRCodeUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QRCodeApplicationTests {

    @Test
    public void contextLoads() {
        String content = "https://www.clzblog.site";
        String bgImgPath = "/Users/chengli.zou/IdeaProjects/qrcode/src/main/resources/static/img/ubuntu-logo-32.png";
        String destPath = "/Users/chengli.zou/IdeaProjects/qrcode/src/main/resources/static/img/new_qrcode.jpg";
        try {
            QRCodeUtils.encode(content, bgImgPath, destPath, true);
            String decode = QRCodeUtils.decode(destPath);
            System.out.println(decode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
