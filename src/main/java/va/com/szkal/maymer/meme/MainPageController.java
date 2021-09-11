package va.com.szkal.maymer.meme;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainPageController {
    private final MemeRepository memeRepository;

    @GetMapping
    public ModelAndView mainPage(@RequestParam(defaultValue = "0") long id) {
        var model = new ModelAndView("index");
        model.addObject("memes", memeRepository.getAll(id));
        return model;
    }
}
