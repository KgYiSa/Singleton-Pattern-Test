package com.mj.tcs.api.v1.web;

import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.util.TCSDtoUtils;
import com.mj.tcs.util.TCSXmlUtils;
import com.mj.tcs.util.OpenTCSParser;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

/**
 * @author Wang Zhen
 */
@Controller
@RequestMapping({"/api/v1/web/utils", "/web/utils"})
public class UtilController extends ServiceController {

    @RequestMapping(value = "/toUpload")
    public String toUpload(HttpServletRequest req, Principal principal, Model model) {
        return "upload";
    }

    @RequestMapping(value="/upload", method= RequestMethod.POST)
    public @ResponseBody
    String handleFileUpload(@RequestParam("file") MultipartFile file, Principal principal) {

        if (!file.isEmpty()) {

            try {
                SceneDto sceneDto = new SceneDto();
                OpenTCSParser openTCSParser = new OpenTCSParser(sceneDto);

                Document document = TCSXmlUtils.getDocument(file.getInputStream());
                Element root = document.getRootElement();

                // Get filename & check
                String filename = file.getOriginalFilename();
                if (!filename.endsWith(".opentcs")) {
                    throw new Exception("The file is an invalid tcs file!");
                }
                // remove suffix
                filename = filename.substring(0, filename.length() - 8);
                sceneDto.setName(filename);

                String userName = principal.getName();
                sceneDto.setAuditorDto(TCSDtoUtils.createAudiorDto(userName));

                List<Element> elements = root.elements();
                for (Element e : elements) {
                    String type = TCSXmlUtils.getNodeAttrMap(e).get("type");
                    openTCSParser.map2Dto(TCSXmlUtils.getMapFromElement(e),type);
                }

                SceneDto newSceneDto = TCSDtoUtils.resolveSceneDtoRelationships(sceneDto);
                newSceneDto = getService().createScene(newSceneDto);

                return "You successfully uploaded -uploaded !";
            } catch (Exception e) {
                return "You failed to upload, ERROR: " + e.getMessage();
            }
        } else {
            return "You failed to upload , because the file was empty.";
        }
    }

}
