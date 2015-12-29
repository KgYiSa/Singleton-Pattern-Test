package com.mj.tcs.api.v1.web;

import com.mj.tcs.api.v1.dto.BlockDto;
import com.mj.tcs.api.v1.dto.PathDto;
import com.mj.tcs.api.v1.dto.PointDto;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.ResourceUnknownException;
import com.mj.tcs.service.Xml2EntityService;
import com.mj.tcs.util.TcsXmlUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;
import java.util.Date;

/**
 * @author Wang Zhen
 */
//@RestController
@Controller
@RequestMapping(value = "/")
public class IndexController extends ServiceController {

    @Autowired
    Xml2EntityService xml2EntityService;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(HttpServletRequest req, Principal principal, Model model){
        System.out.println(req.getRequestURI());
        model.addAttribute("date", new Date());
        return "login";
    }

    @RequestMapping("favicon.ico")
    String favicon() {
        return "forward:/resources/images/favicon.ico";
    }

//    @RequestMapping(value = "/hello")
//    public String hello(HttpServletRequest req, Principal principal, Model model) {
//        System.out.println("come to hello ");
//        System.out.println(req.getContextPath());
//
//        return "index";
//    }

    @RequestMapping(value = "/operating")
    public String operate(HttpServletRequest req, Principal principal, Model model) {
        return "operating";
    }

    @RequestMapping(value = "/modelling")
    public String model(HttpServletRequest req, Principal principal, Model model) {
        return "modelling";
    }

    @RequestMapping(value = "/toUpload")
    public String toUpload(HttpServletRequest req, Principal principal, Model model) {
        System.out.print("toUpload");
        return "upload";
    }

    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {

        Document document = TcsXmlUtils.getDocument("D:\\Demo.xml");
        Element root = document.getRootElement();
        String newSceneName = "test_scene_" +
                new SimpleDateFormat("yy_MM_dd_HH_mm_ss").format(new Date()).toString();
        System.out.println(newSceneName);

        SceneDto sceneDto = new SceneDto();
        sceneDto.setAuditorDto(xml2EntityService.createAuditor());
        sceneDto.setName(newSceneName);
        xml2EntityService.setSceneDto(sceneDto);
        List<Element> elements = root.elements();
        for (Element e : elements) {
            String type = TcsXmlUtils.getNodeAttrMap(e).get("type");
            xml2EntityService.Map2Dto(TcsXmlUtils.getPoint(e),type);
        }

        SceneDto newSceneDto = resolveRelationships(sceneDto);
        newSceneDto = getService().createScene(newSceneDto);
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody
    String handleFileUpload(@RequestParam("name") String name,
                            @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }


    private SceneDto resolveRelationships(SceneDto sceneDto) {
        if (sceneDto.getPointDtos() != null) {
            sceneDto.getPointDtos().forEach(p -> {
                p.setSceneDto(sceneDto);
                if (p.getIncomingPaths() != null) {
                    p.setIncomingPaths(p.getIncomingPaths().stream()
                            .map(pa -> {
                                final PathDto tempPa = Objects.requireNonNull(sceneDto.getPathDtoByUUID(pa.getUUID()));
                                tempPa.setDestinationPointDto(p);
                                return tempPa;
                            })
                            .collect(Collectors.toSet()));
                }
                System.out.print("");
                if (p.getOutgoingPaths() != null) {
                    p.setOutgoingPaths(p.getOutgoingPaths().stream()
                            .map(pa -> {
                                final PathDto tempPa = Objects.requireNonNull(sceneDto.getPathDtoByUUID(pa.getUUID()));
                                tempPa.setSourcePointDto(p);
                                return tempPa;
                            })
                            .collect(Collectors.toSet()));
                }
            });
        }
        if (sceneDto.getPathDtos() != null) {
            sceneDto.getPathDtos().forEach(p -> {
                p.setSceneDto(sceneDto);
                if (p.getSourcePointDto() == null || p.getDestinationPointDto()  == null) {
                    throw new ObjectUnknownException("Path  : " + p.getName() + " point(s) is null!");
                }
            });
        }
        if (sceneDto.getLocationTypeDtos() != null) {
            sceneDto.getLocationTypeDtos().forEach(t -> t.setSceneDto(sceneDto));
        }
        if (sceneDto.getLocationDtos() != null) {
            sceneDto.getLocationDtos().forEach(l -> {
                l.setSceneDto(sceneDto);
                if (l.getAttachedLinks() != null) {
                    l.getAttachedLinks().forEach(li -> {
                        li.setSceneDto(sceneDto);

                        PointDto linkedPointDto = Objects.requireNonNull(sceneDto.getPointDtoByUUID(li.getPointDto().getUUID()));
                        li.setPointDto(linkedPointDto);
                        linkedPointDto.addAttachedLinks(li);
                        li.setLocationDto(sceneDto.getLocationDtoByUUID(li.getLocationDto().getUUID()));
                    });
                }

                l.setLocationTypeDto(sceneDto.getLocationTypeDtoByUUID(l.getLocationTypeDto().getUUID()));
            });
        }
        if (sceneDto.getBlockDtos() != null) {
            sceneDto.getBlockDtos().forEach(b -> {
                b.setSceneDto(sceneDto);

                // UUID -> Point/Path/Block
                if (b.getMembers() != null) {
                    Set<BlockDto.BlockElementDto> elementDtos = new LinkedHashSet<>();
                    for (BlockDto.BlockElementDto mem : b.getMembers()) {
                        String uuid = Objects.requireNonNull(mem.getUUID());

                        // Point
                        BaseEntityDto memDto = sceneDto.getPointDtoByUUID(uuid);
                        if (memDto != null) {
                            elementDtos.add(new BlockDto.BlockElementDto(memDto));
                            continue;
                        }

                        // Path
                        memDto = sceneDto.getPathDtoByUUID(uuid);
                        if (memDto != null) {
                            System.out.print("s");
                            elementDtos.add(new BlockDto.BlockElementDto(memDto));
                            continue;
                        }

                        // Location
                        memDto = sceneDto.getLocationDtoByUUID(uuid);
                        if (memDto != null) {
                            elementDtos.add(new BlockDto.BlockElementDto(memDto));
                            continue;
                        }

                        throw new ResourceUnknownException("Resource: " + uuid + " not found!");
                    }
                    b.setMembers(elementDtos);
                }
            });
        }
        if (sceneDto.getStaticRouteDtos() != null) {
            sceneDto.getStaticRouteDtos().forEach(r -> {
                r.setSceneDto(sceneDto);

                r.setHops(r.getHops().stream().map(p -> sceneDto.getPointDtoByUUID(p.getUUID())).collect(Collectors.toList()));
            });
        }
        if (sceneDto.getVehicleDtos() != null) {
            sceneDto.getVehicleDtos().forEach(v -> {
                v.setSceneDto(sceneDto);

                if (v.getInitialPoint() != null) {
                    v.setInitialPoint(sceneDto.getPointDtoByUUID(v.getInitialPoint().getUUID()));
                }
            });
        }

        return sceneDto;
    }
}