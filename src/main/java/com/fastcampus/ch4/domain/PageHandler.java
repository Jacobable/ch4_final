package com.fastcampus.ch4.domain;

import org.springframework.web.util.UriComponentsBuilder;

public class PageHandler {
    //    private int page;
//    private int pageSize;
//    private String option;
//    private String keyword;
    private  SearchCondition sc;

    private int totalCnt; // 게시물의 총 갯수
    private int totalPage; // 전체 페이지의 갯수
    private  int naviSize=10;
    private int beginPage; // 화면에 보여줄 첫 페이지
    private int endPage; // 화면에 보여줄 마지막 페이지
    private boolean showNext; // 이후를 보여줄지의 여부. endPage==totalPage이면, showNext는 false
    private boolean showPrev; // 이전을 보여줄지의 여부. beginPage==1이 아니면 showPrev는 false

    public PageHandler(int totalCnt,SearchCondition sc){
        this.totalCnt=totalCnt;
        this.sc=sc;

        dopaging(totalCnt,sc);
    }

    public void dopaging(int totalCnt,SearchCondition sc){
        this.totalCnt=totalCnt;


        totalPage=(int)Math.ceil(totalCnt/(double)sc.getPageSize());
        beginPage=(sc.getPage()-1)/naviSize*naviSize+1;
        endPage=Math.min(beginPage+naviSize-1,totalPage);
        showPrev=beginPage!=1;
        showNext=endPage!=totalPage;

    }
    public SearchCondition getSc() {
        return sc;
    }

    public void setSc(SearchCondition sc) {
        this.sc = sc;
    }

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getNaviSize() {
        return naviSize;
    }

    public void setNaviSize(int naviSize) {
        this.naviSize = naviSize;
    }


    public int getBeginPage() {
        return beginPage;
    }

    public void setBeginPage(int beginPage) {
        this.beginPage = beginPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }

    public boolean isShowPrev() {
        return showPrev;
    }

    public void setShowPrev(boolean showPrev) {
        this.showPrev = showPrev;
    }

    void print (){
        System.out.println("page="+sc.getPage());
        System.out.println(showPrev?"[PREV]":"");
        for(int i=beginPage;i<=endPage;i++){
            System.out.println(i+"");
        }
        System.out.println(showNext?"[NEXT]":"");
    }


    @Override
    public String toString() {
        return "PageHandler{" +
                "sc=" + sc +
                ", totalCnt=" + totalCnt +
                ", totalPage=" + totalPage +
                ", naviSize=" + naviSize +
                ", beginPage=" + beginPage +
                ", endPage=" + endPage +
                ", showNext=" + showNext +
                ", showPrev=" + showPrev +
                '}';
    }
}
