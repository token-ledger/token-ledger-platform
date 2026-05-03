package com.ledger.tokenledgercloud.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ledger.tokenledgercloud.dto.MemberResponse;
import com.ledger.tokenledgercloud.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

	private final MemberService memberService;

	@GetMapping("/")
	public String home(Authentication authentication, Model model) {
		MemberResponse me = memberService.getCurrentMember(authentication);
		model.addAttribute("member", me);
		return "home";
	}

	@GetMapping("/login")
	public String loginPage(@RequestParam(required = false) String error,
							@RequestParam(required = false) String logout,
							Authentication authentication,
							Model model) {
		if (authentication != null && authentication.isAuthenticated()
			&& !"anonymousUser".equals(authentication.getPrincipal())) {
			return "redirect:/";
		}
		if (error != null) {
			model.addAttribute("errorMessage", "로그인에 실패했어요. 다시 시도해주세요.");
		}
		if (logout != null) {
			model.addAttribute("infoMessage", "로그아웃되었습니다.");
		}
		return "login";
	}
}
