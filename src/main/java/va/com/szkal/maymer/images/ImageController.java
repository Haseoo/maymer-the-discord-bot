package va.com.szkal.maymer.images;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import va.com.szkal.maymer.Env;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/img")
@RequiredArgsConstructor
public class ImageController {

    private final ImageStoreService imageStoreService;
    private final Env env;

    @GetMapping
    public String init(@RequestParam String id, HttpServletResponse response) {
        if (imageStoreService.isTokenPresent(id)) {
            Cookie cookie = new Cookie(ImageStoreService.COOKIE_JWT, imageStoreService.getImageJwt(id));
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setDomain(env.getDomain());
            cookie.setMaxAge(ImageStoreService.TOKEN_EXPIRATION);
            cookie.setSecure(true);
            response.addCookie(cookie);
            imageStoreService.deleteToken(id);
            return "redirect:" + env.getDscViewerRedirectUrl() + "/";
        } else {
            return "linkExpired";
        }
    }
}
