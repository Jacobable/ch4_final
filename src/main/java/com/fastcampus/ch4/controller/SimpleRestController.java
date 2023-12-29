package com.fastcampus.ch4.controller;

import com.fastcampus.ch4.domain.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
// @ResponseBody를 사용하는 메서드가 여러개일때 클래스에
//@RestController를 사용하면 @ResponseBody(메서드 앞에) 생략가능
public class SimpleRestController {
//    @GetMapping("/ajax")
//    public String ajax() {
//        return "ajax";
//    }
@GetMapping("/test")
public String test() {
    return "test";
}//뷰이름임

    @PostMapping("/send")
    @ResponseBody
    public Person test(@RequestBody Person p) {
        System.out.println("p = " + p);
        p.setName("ABC");
        p.setAge(p.getAge() + 10);

        return p;
    }
}