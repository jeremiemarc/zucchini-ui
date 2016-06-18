package io.zucchiniui.capsule;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UIController {

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping({"/", "/ui"})
    public String rootToUi() {
        return "redirect:/ui/";
    }

    @GetMapping("/ui/")
    public String displayFrontend() {
        return "forward:/ui/index.html";
    }

    @GetMapping("/ui/scripts/config.js")
    public void uiConfig(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final UIConfig uiConfig = new UIConfig();
        uiConfig.setBackendBaseUri(getBackendBaseUri(request));

        response.setStatus(200);
        response.setContentType("application/javascript");

        final String serializedConfig = objectMapper.writeValueAsString(uiConfig);
        response.getOutputStream().print("var configuration = " + serializedConfig + ";");
    }

    private String getBackendBaseUri(final HttpServletRequest request) {
        final StringBuilder baseUri = new StringBuilder();
        baseUri.append(request.getScheme()).append("://").append(request.getServerName());

        final boolean addPort;
        switch (request.getScheme()) {
            case "http":
                addPort = (request.getServerPort() != 80);
                break;
            case "https":
                addPort = (request.getServerPort() != 443);
                break;
            default:
                addPort = true;
                break;
        }
        if (addPort) {
            baseUri.append(':').append(request.getServerPort());
        }

        return baseUri.toString();
    }

}
