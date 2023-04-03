package matjibback.controller;

import matjibback.entity.Bookmark;
import matjibback.entity.Member;
import matjibback.entity.Restaurant;
import matjibback.repository.BookmarkRepository;
import matjibback.repository.RestaurantRepository;
import matjibback.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class BookmarkController {
    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    RestaurantRepository restaurantRepository;

    //북마크 리스트 가져오기(Archive)
    @GetMapping("matjib/bookmark/get")
    public ResponseEntity getBookmark(@RequestBody Map<String, String> memInfo, HttpServletResponse res) {
        //1. 토큰 유효성 검사 후 멤버 객체 반환
        Map<Boolean, Member> result = memberService.validateTokenAndGetMemberById(memInfo.get("token"));
        if (result.containsKey(false)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);   //토큰이 유효하지 않은 경우
        } else if (result.get(true) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);      //토큰은 유효하나 멤버 객체가 없을 때
        }
        //2. 오류 없을 시 멤버 객체 가져오기
        Member member = result.get(true);
        int memId = member.getId();     //멤버id가져오기

        //3. 북마크한 음식점 가져오기
        //해당 멤버가 북마크 한 리스트(a) 가져오기
        List<Bookmark> bookmarkList = bookmarkRepository.findBookmarkByMemId(memId);
        //리스트(a)에서 음식점id만 모아서 리스트(b) 만들기
        List<Integer> resIds = bookmarkList.stream().map(Bookmark::getResId).collect(Collectors.toList());
        //리스트(b)에서 음식점 객체 모아서 리스트 만들기
        List<Restaurant> restaurants = restaurantRepository.findRestaurantByIdIn(resIds);

        //4. 음식점 객체, 응답코드 보내기
        return new ResponseEntity(restaurants, HttpStatus.OK);
    }

    //북마크 추가(지도API)
    @PutMapping("/matjib/bookmark/add")
    public ResponseEntity addBookmark(@RequestBody Map<String, String> memInfo, HttpServletResponse res) {
        Map<Boolean, Member> result = memberService.validateTokenAndGetMemberById(memInfo.get("token"));
        if (result.containsKey(false)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (result.get(true) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Member member = result.get(true);
        int resId = Integer.parseInt(memInfo.get("resId"));

        Bookmark bookmark = new Bookmark();
        bookmark.setMemId(member.getId());
        bookmark.setResId(resId);
        bookmarkRepository.save(bookmark);

        return new ResponseEntity(resId, HttpStatus.OK);
    }

    //북마크 삭제(Archive, 지도API)
    @GetMapping("/matjib/bookmark/delete")
    public ResponseEntity deleteBookmark(@RequestBody Map<String, String> memInfo, HttpServletResponse res) {
        Map<Boolean, Member> result = memberService.validateTokenAndGetMemberById(memInfo.get("token"));
        if (result.containsKey(false)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (result.get(true) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Member member = result.get(true);
        int resId = Integer.parseInt(memInfo.get("resId"));
        int memId = member.getId();

        bookmarkRepository.deleteBookmarkByResIdAndMemId(resId, memId);

        return new ResponseEntity(resId, HttpStatus.OK);
    }
}
