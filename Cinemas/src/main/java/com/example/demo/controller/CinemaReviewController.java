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
import com.example.demo.entity.RetentionUser;
import com.example.demo.form.AccountForm;
import com.example.demo.form.CinemasPostForm;
import com.example.demo.form.SessionForm;
import com.example.demo.repositry.AccountRepository;
import com.example.demo.repositry.CinemasPostRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.CinemasPostService;

@Controller
@SessionAttributes("sessionForm")
public class CinemaReviewController {

	@Autowired //処理を行うserviceインジェクション。newしてくれる
	AccountService accountService;

	@Autowired
	CinemasPostService cinemasPostService;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	CinemasPostRepository cinemasPostRepository;

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
	
	@ModelAttribute("retentionUser")
	public RetentionUser geRetentionUser() {
		return new RetentionUser();
	}
	
	@GetMapping("/top")
	public String topview() {
		return "top"; // top.htmlに飛ぶ
	}

	@GetMapping("/login")
	public String LoginView() {
		return "login"; // login.htmlに飛ぶ
	}

	@GetMapping("/register")
	public String registerView() {
		return "register"; // register.htmlに飛ぶ
	}

	@GetMapping("/posting")
	public String postView() {
		// ユーザー情報を使って該当のユーザーの直近の投稿を取得
		return "posting";
	}

	@GetMapping("/logout")
	public String logoutHandle(@ModelAttribute("sessionForm") SessionForm sessionForm) {
		sessionForm = null; // sessionFormに保存している値をリセット
		return "logout";
	}

	@GetMapping("myPage")
	public String myPage(Model model) {
	   
//        List<RetentionUser> resultUserList = accountRepository.getRetentionUser();    // accountRepositoryを使ってユーザー情報を取得
//        model.addAttribute("resultUserList", resultUserList);                                // モデルにユーザー情報をセットしてThymeleafテンプレートに渡す

		List<CinemasPost> mySelfPostList =  cinemasPostService.findMyPost();  // ユーザーが投稿したものだけ表示
		model.addAttribute("mySelfPostList", mySelfPostList);
		return "myPage";
	}

	@PostMapping("/loginPass")
	//login.thmlからsessionFomに格納され送られてきた情報を照合するメソッド
	// top.htmlで値を格納したsessionFormオブジェクトを使用。()内でもう一度newさてれて値を受け取っている
	public String topLoginResult(Model model, @ModelAttribute("sessionForm") SessionForm sessionForm)
			throws NoSuchAlgorithmException {

		String loginResult = null;                         // DBの照合結果に応じて遷移先を変えたいので、いったんnull
		//		System.out.println("controller53line User_name: " + sessionForm.getPassword());
		String result = accountService.loginCheck(sessionForm);
		if (result != null) {
			sessionForm.setUser_name(result);  // sessionFormに値が入っていなかったnameフィールドをここでセット
			loginResult = "myPage";                     // login成功→myPage.htmlへ遷移
			
			// ユーザーが投稿したものを一覧表示する
			List<CinemasPost> mySelfPostList =  cinemasPostService.findMyPost();
			model.addAttribute("mySelfPostList", mySelfPostList);
			
			// ユーザーの情報を受け取り保持する
			List<RetentionUser> resultUserList = accountService.saveUser(null);
			model.addAttribute("resultUserList", resultUserList);
			
		} else {                                                      // resultがnullということは、DBに一致するレコードがなかったということ
			loginResult = "login";                          // login失敗→login.htmlへ遷移
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
		// Listの０番目のuser_idを取得
//		System.out.println(cinemasPostsList.get(0).getUser_id());
//		System.out.println(cinemasPostsList.get(0).getTitle());
		//		<p>[[${cinemasPost.user_name}]] : [[${cinemasPost.title}]]</p>

		return "main";
	}

	// cinemaPostrメソッドに飛ぶ
	@PostMapping("cinemaPosts")
	public String insertPost(Model model,
			// @validatedで入力値チェックして結果をBindingResultで受け取る。@NotBlankの処理
			@Validated CinemasPostForm cinemasPostForm, BindingResult bindingResult,
			// Modelに格納されたSessionFormのobjectのsessionForm keyを取得
			@ModelAttribute("sessionForm") SessionForm sessionForm) {

		if (bindingResult.hasErrors()) {
			// つぶやき全件取得。modelオブジェクトに保存したcinemasPostListはスコープで既に消えているため、再度取得
			List<CinemasPost> cinemasPostList = cinemasPostService.findAllPost();
			model.addAttribute("cinemasPostList", cinemasPostList);
			return "main";
		}

		//  CinemasPost entityに投稿情報を格納
		// postidはDBにおいてsirialが設定されているためnull。自動連番で生成されるのでnullにする
		// title,はcinemasPostFormから、user_idはsessionFormから取り出す
		CinemasPost cinemasPost = new CinemasPost(null, sessionForm.getUser_id(), cinemasPostForm.getTitle(),
				cinemasPostForm.getImpressions());

		// DBに追加したい情報を格納したcinemasPostオブジェクトをサービスに渡す
		boolean result = cinemasPostService.createNewPost(cinemasPost);
		// trueの場合は投稿成功
		if (result) {
			model.addAttribute("message", "投稿しました。");
		} else {
			// 投稿が失敗(false)したら通るブロック
			model.addAttribute("msg", "投稿できませんでした。");
		}
		// insertを実行したら、改めて全投稿情報を取得
		List<CinemasPost> cinemasPostList = cinemasPostService.findAllPost();
		model.addAttribute("cinemasPostList", cinemasPostList);
		return "posting";
	}

	@PostMapping("registerCheck") // register.htmlでの入力されaccountFormに格納された情報をチェックする
	public String registerCheck(Model model, // 新規会員登録結果を文字列をModelに格納
			@Validated AccountForm aF, // register.htmlの入力フォーム内容のチェック結果が格納されている
			BindingResult bindingResult)
			throws NoSuchAlgorithmException { // 例外処理
		if (bindingResult.hasErrors()) { // 何かしらの入力エラーがあった場合はtrue
			return "register";
		}

		// AccountForm→accounts entityに値を移す
		Account registerAccount = new Account(aF.getUser_id(), aF.getUser_name(), aF.getName(), aF.getPassword(),
				aF.getMail());

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
		return "registrationResult"; // registrationResult.htmlに飛ぶ
	}

}
