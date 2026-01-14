package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.entity.Local;
import com.inmap.restfulApiInMap.repository.LocalRepository;
import com.inmap.restfulApiInMap.service.LocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
public class LocalController {

    @Autowired
    private LocalService localService;
    @GetMapping("/findAllLocals")
    public List<Local> findAllLocal(){
        return localService.findAllLocals();
    }
    @GetMapping("/findLocalByNameWithJPQL/{name}")
    Optional<Local> findLocalByNameWithJPQL(@PathVariable  String name){
        return localService.findLocalByNameWithJPQL(name);
    }
    @GetMapping("/findByName/{name}")
    Optional<Local> findByName(@PathVariable String name){
        return localService.findByName(name);
    }
    @GetMapping("/findByNameIgnoreCase/{name}")
    Optional<Local> findByNameIgnoreCase(@PathVariable String name){
        return localService.findByNameIgnoreCase(name);
    }
    @PostMapping("/saveLocal")
    public Local saveLocal(@RequestBody Local local){
        return localService.saveLocal(local);
    }
    @PutMapping("/updateLocal/{id}")
    public Local updateLocal(@PathVariable long id, @RequestBody Local local){
        return  localService.updateLocal(id,local);
    }
    @DeleteMapping("/deleteLocal/{id}")
    public String deleteLocal(@PathVariable long id){
        localService.deleteLocal(id);
        return "Su registro ha sido eliminado existosamente";
    }
}
