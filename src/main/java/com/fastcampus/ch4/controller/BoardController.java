package com.fastcampus.ch4.controller;

import com.fastcampus.ch4.Service.BoardService;
import com.fastcampus.ch4.domain.BoardDto;
import com.fastcampus.ch4.domain.PageHandler;
import com.fastcampus.ch4.domain.SearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {
    @Autowired
    BoardService boardService;

    @PostMapping("/modify")
    public String modify(BoardDto boardDto,Model m,HttpSession session, RedirectAttributes rattr){
        String writer=(String) session.getAttribute("id");//getAttribute반환하는 값이 object라서 형변환 필수
        boardDto.setWriter(writer);

        try {
            int rowCnt= boardService.modify(boardDto);//insert
            if(rowCnt!=1) throw  new Exception("Modify failed");

            rattr.addFlashAttribute("msg","MOD_OK");

            return "redirect:/board/list";
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute(boardDto);
            rattr.addFlashAttribute("msg","MOD_ERR");
            return "board";
        }
    }

    @PostMapping("/write")
    public String write(BoardDto boardDto,Model m,HttpSession session, RedirectAttributes rattr){
        String writer=(String) session.getAttribute("id");//getAttribute반환하는 값이 object라서 형변환 필수
        boardDto.setWriter(writer);

        try {
         int rowCnt= boardService.write(boardDto);//insert
            if(rowCnt!=1) throw  new Exception("Write failed");

            rattr.addFlashAttribute("msg","WRT_OK");

            return "redirect:/board/list";
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute(boardDto);
            rattr.addFlashAttribute("msg","WRT_ERR");
            return "board";
        }
    }

    @GetMapping("/write")
    public String write(Model m){
        m.addAttribute("mode","new");
        return "board"; //읽기와 쓰기에 사용,쓰기에 사용할때는 mode=new
    }

    @PostMapping("/remove")
    public String remove(Integer bno, Integer page, Integer pageSize, Model m, HttpSession session, RedirectAttributes rattr){
        String writer =(String) session.getAttribute("id");
        try {
            m.addAttribute("page",page);
            m.addAttribute("pageSize",pageSize);

            int rowCnt=boardService.remove(bno,writer);

            if(rowCnt!=1)
                throw new Exception("board remove error");

            rattr.addFlashAttribute("msg","DEL_OK");//세션을 이용한 일회성 저장



        } catch (Exception e) {
            e.printStackTrace();
            rattr.addFlashAttribute("msg","DEL_ERR");//model에 안넘기고 addFlashAttribute로 rattr에 넘기면 한번만 나옴.대신 rattr을 사용하면 jsp에서 param사용X
        }

        return "redirect:/board/list";
    }

    @GetMapping("/read")
    public String read(Integer bno,Integer page,Integer pageSize,Model m){
        try {
           BoardDto boardDto= boardService.read(bno);//BoardDto 타입
           //m.addAttribute("boardDto",boardDto); 타입의 첫글자를 소문자로한걸 자동으로 이름으로 넣어줄수있음.아래문장과 동일
           m.addAttribute(boardDto);//뷰로 보내는모델에 값을 넣기
            m.addAttribute("page",page);
            m.addAttribute("pageSize",pageSize);

        } catch (Exception e) {
           e.printStackTrace();
        }
        return "board";
    }

    @GetMapping("/list")
    public String list(SearchCondition sc, Model m, HttpServletRequest request) {
        if(!loginCheck(request))
            return "redirect:/login/login?toURL="+request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동

        try {
            int totalCnt=boardService.getSearchResultCnt(sc);
            m.addAttribute("totalCnt",totalCnt);

            PageHandler pageHandler=new PageHandler(totalCnt,sc);


         List<BoardDto> list= boardService.getSearchResultPage(sc);
         m.addAttribute("list",list);
         m.addAttribute("ph",pageHandler);

            Instant startOfToday= LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
            m.addAttribute("startOfToday",startOfToday.toEpochMilli());

        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("msg","LIST_ERR");
            m.addAttribute("totalCnt",0);
        }

        return "boardList"; // 로그인을 한 상태이면, 게시판 화면으로 이동
    }

//    @GetMapping("/list")
//    public String list(Integer page, Integer pageSize, Model m, HttpServletRequest request) {
//        if(!loginCheck(request))
//            return "redirect:/login/login?toURL="+request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동
//
//        if(page==null) page=1;
//        if(pageSize==null) pageSize=10;
//
//        try {
//            int totalCnt=boardService.getCount();
//            PageHandler pageHandler=new PageHandler(totalCnt,page,pageSize);
//
//            Map map=new HashMap();
//            map.put("offset",(page-1)*pageSize);
//            map.put("pageSize",pageSize);
//
//
//            List<BoardDto> list= boardService.getPage(map);
//            m.addAttribute("list",list);
//            m.addAttribute("ph",pageHandler);
//            m.addAttribute("page",page);
//            m.addAttribute("pageSize",pageSize);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "boardList"; // 로그인을 한 상태이면, 게시판 화면으로 이동
//    }

    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션을 얻어서
        HttpSession session = request.getSession();
        // 2. 세션에 id가 있는지 확인, 있으면 true를 반환
        return session.getAttribute("id")!=null;
    }
}