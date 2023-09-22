package com.example.demo.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.entity.Account;
import com.example.demo.entity.CinemasPost;
import com.example.demo.form.AccountForm;
import com.example.demo.form.CinemasPostForm;
import com.example.demo.form.SessionForm;
import com.example.demo.service.AccountService;
import com.example.demo.service.CinemasPostService;

@Controller
@SessionAttributes("sessionForm")
public class CinemaReviewController {

	@Autowired //処理を行うserviceインジェクション。newしてくれる
	AccountService accountService;

	@Autowired
	CinemasPostService cinemasPostService;

	@ModelAttribute("sessionForm") // ログイン情報を扱うformクラスの準備
	public SessionForm getSessionForm() {
		return new SessionForm();
	}

	@ModelAttribute("accountForm") // account情報を扱うformクラスの準備
	public AccountForm getAccountForm() {
		return new AccountForm();
	}

	@ModelAttribute("cinemasPostForm") // cinemasPost情報を扱うformクラスの準備
	public CinemasPostForm getCinemaspostForm() {
		return new CinemasPostForm();
	}

	@GetMapping("/top")
	public String topview() {
		return "top";             // top.htmlに飛ぶ
	}

	@GetMapping("/login")
	public String LoginView() {
		return "login";           // login.htmlに飛ぶ
	}

	@GetMapping("/register")
	public String registerView() {
		return "register";      // register.htmlに飛ぶ
	}

	@GetMapping("/posting")
	public String postView() {
        return "post";   // posting.htmlに飛ぶ
    }

	@GetMapping("/logout")
	public String logoutHandle(@ModelAttribute("sessionForm") SessionForm sessionForm) {
		sessionForm = null;  // sessionFormに保存している値をリセット
		return "logout";
	}
	
	@GetMapping("myPage")
	public String myPageView() {
		return "myPage";
	}

	@PostMapping("/loginPass")
	//login.thmlからsessionFomに格納され送られてきた情報を照合するメソッド
	// top.htmlで値を格納したsessionFormオブジェクトを使用。()内でもう一度newさてれて値を受け取っている
	public String topLoginResult(@ModelAttribute("sessionForm") SessionForm sessionForm)
			throws NoSuchAlgorithmException {

		String loginResult = null; // DBの照合結果に応じて遷移先を変えたいので、いったんnull
		//		System.out.println("controller53line User_name: " + sessionForm.getPassword());
		String result = accountService.loginCheck(sessionForm);
		if (result != null) {
			sessionForm.setUserName(result); // sessionFormに値が入っていなかったnameフィールドをここでセット
			loginResult = "myPage";
		} else { // resultがnullということは、DBに一致するレコードがなかったということ
			loginResult = "login";
		}
		//		System.out.println("controller61line User_name: " + sessionForm.getUser_name());
		//		sessionForm.setUser_name(result);
		return loginResult;
	}

	// main viewに飛んで一覧表示
	@GetMapping("main")
	public String mainView(Model model) {
		// 投稿全件取得
		List<CinemasPost> cinemasPostsList = cinemasPostService.findAllPost();
		// 投稿をcinemasPostsList)に格納した全件をmainに表示
		model.addAttribute("cinemasPostsList", cinemasPostsList);
		return "main";
	}

	// cinemaPostrメソッドに飛ぶ
	@PostMapping("cinemaPosts")
	public String insertMutter(Model model,
			// @validatedで入力値チェックして結果をBindingResultで受け取る。@NotBlankの処理
			@Validated CinemasPostForm cinemasPostForm, BindingResult bindingResult,
			// Modelに格納されたSessionFormのobjectのsessionForm keyを取得
			@ModelAttribute("sessionForm") SessionForm sessionForm) {

		if (bindingResult.hasErrors()) {
			// つぶやき全件取得。modelオブジェクトに保存したmutterListはスコープで既に消えているため、再度取得
			List<CinemasPost> cinemasPostList = cinemasPostService.findAllPost();
			model.addAttribute("cinemasPostList", cinemasPostList);
			return "main";
		}
		//		System.out.println("C : 107");

		// entityに投稿情報を格納
		// postidはDBにおいてsirialが設定されているためnull。自動連番で生成されるのでnullにする
		CinemasPost cinemasPost = new CinemasPost(null, sessionForm.getUserName(), cinemasPostForm.getTitle(),
				cinemasPostForm.getImpressions(), cinemasPostForm.getRating());
		// title,はcinemasPostFormから、nameはsessionFormから取り出す

		// DBに追加したい情報を格納したcinemasPostオブジェクトをサービスに渡す
		boolean result = cinemasPostService.createNewPost(cinemasPost);
		// trueの場合は投稿成功
		if (!result) { // 投稿が失敗(false)したら通るブロック
			model.addAttribute("msg", "投稿できませんでした");
		}
		// insertを実行したら、改めて全投稿情報を取得
		List<CinemasPost> cinemasPostList = cinemasPostService.findAllPost();
		model.addAttribute("cinemasPostList", cinemasPostList);
		return "main";
	}

	@PostMapping("registerCheck")                      // register.htmlでの入力されaccountFormに格納された情報をチェックする
	public String registerCheck(Model model,      // 新規会員登録結果を文字列をModelに格納
			@Validated AccountForm aF,     // register.htmlの入力フォーム内容のチェック結果が格納されている
			BindingResult bindingResult)
			throws NoSuchAlgorithmException {      // 例外処理
		if (bindingResult.hasErrors()) {                    // 何かしらの入力エラーがあった場合はtrue
			return "register";
		}

		// AccountForm→accounts entityに値を移す
		Account registerAccount = new Account(aF.getUserId(), aF.getUserName(), aF.getPassword() , aF.getName(), aF.getMail());
		
		// DBにすでに存在するか確認。trueだったら重複レコード無し、falseなら重複レコードあり
		boolean beforeCheck = accountService.registerCheck(registerAccount);
		if (!beforeCheck) { // beforeCheckの値がfalse（DBに同じレコードがあった場合）の場合にifブロック実行
			model.addAttribute("registerResult", "入力したユーザー情報は既に登録済みです。");
			return "register";
		}
		// 新規会員登録が実行できる場合は以下の処理に進む
		// 登録したい情報を格納したentityをメソッドの引数に渡してあげる
		boolean result = accountService.createNewAccount(registerAccount);
		if (result) { // レコードが一件追加できたときにこのifブロックを実行
			// modelのregisterResultに文字列を格納
			model.addAttribute("registerResult", "登録完了！");
		} else {
			model.addAttribute("registerResult", "登録失敗！");
		}
		return "registrationResult";   // registrationResult.htmlに飛ぶ
	}

}
