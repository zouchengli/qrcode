package site.clzblog.qrcode.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.clzblog.qrcode.constants.QRCodeStatus;
import site.clzblog.qrcode.utils.QRCodeUtils;
import site.clzblog.qrcode.utils.RedisUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Controller
public class QRCodeController {
    private final static String BASE_PATH = "/Users/chengli.zou/IdeaProjects/qrcode/src/main/resources/static/img/";

    private final static String JPG_SUFFIX = ".jpg";

    private final RedisUtils redisUtils;

    public QRCodeController(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @RequestMapping("/")
    public String generateQRCode(Model model) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisUtils.setString(token, QRCodeStatus.SCAN, 600L);
        String content = String.format("http://69jzw8.natappfree.cc/scanning?token=%s", token);
        String bgImgPath = BASE_PATH + "ubuntu-logo-32.png";
        String destPath = String.format("%s%s%s", BASE_PATH, token, JPG_SUFFIX);
        try {
            QRCodeUtils.encode(content, bgImgPath, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("token", token);
        return "index";
    }

    @RequestMapping(value = "get-qrcode", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getQRCode(@RequestParam("token") String token) {
        File file = new File(String.format("%s%s%s", BASE_PATH, token, JPG_SUFFIX));
        byte[] bytes;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            int available = fileInputStream.available();
            bytes = new byte[available];
            int read = fileInputStream.read(bytes, 0, available);
            System.out.println(read);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("scanning")
    public String scanning(@RequestParam("token") String token, Model model) {
        if (StringUtils.isEmpty(token)) return "error";

        String redisToken = redisUtils.getString(token);
        if (StringUtils.isEmpty(redisToken)) return "timeout";

        redisUtils.setString(token, QRCodeStatus.SCANNING, 60L);

        model.addAttribute("token", token);
        return "scanning";
    }

    @RequestMapping("success")
    public String success() {
        return "success";
    }

    @RequestMapping("failure")
    @ResponseBody
    public String failure() {
        return "failure";
    }

    @RequestMapping("confirm-login")
    @ResponseBody
    public Boolean confirmLogin(@RequestParam("token") String token) {
        if (StringUtils.isEmpty(token)) {
            System.out.println("Token is empty");
            return false;
        }

        String redisToken = redisUtils.getString(token);
        if (StringUtils.isEmpty(redisToken)) {
            System.out.println("Redis token not found");
            return false;
        }

        redisUtils.setString(token, QRCodeStatus.SCANNED);

        return true;
    }

    @RequestMapping("check-token")
    @ResponseBody
    public String checkToken(@RequestParam("token") String token) {
        if (StringUtils.isEmpty(token)) return "Redis token is empty";

        String redisToken = redisUtils.getString(token);
        if (StringUtils.isEmpty(redisToken)) return "Redis token not found";

        return redisToken;
    }
}
