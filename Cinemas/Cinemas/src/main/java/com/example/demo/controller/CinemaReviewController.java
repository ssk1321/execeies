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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.entity.Account;
import com.example.demo.entity.RetentionUser;
import com.example.demo.form.AccountForm;
import com.example.demo.form.SessionForm;
import com.example.demo.repositry.AccountRepository;
import com.example.demo.repositry.CinemasPostRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.CinemasPostService;
import com.example.demo.service.EncorderService;

@Controller
@SessionAttributes("sessionForm")
public class CinemaReviewController {

	@Autowired //処理を行うserviceインジェクション。newしてくれる
	AccountService accountService;

	@Autowired
	CinemasPostService cinemasPostService;

	@Autowired
	EncorderService encorderService;

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

	@ModelAttribute("retentionUser") // cinemasPost情報を扱うformクラスの準備
	public RetentionUser geRetentionUser() {
		return new RetentionUser();
	}

	@GetMapping("/top")
	public String topview() {
		return "index"; // index.htmlに飛ぶ
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
	public String postView(Model model, @ModelAttribute("sessionForm") SessionForm sessionForm) {
		if (sessionForm == null || sessionForm.getUser_id() == null) {
			model.addAttribute("msg", "セッションが切れました。使用する際は再びログインしてください。");
			return "posting";
		}
		return "posting";
	}

	@GetMapping("/logout")
	public String logoutHandle(@ModelAttribute("sessionForm") SessionForm sessionForm) {
		sessionForm = null; // sessionFormに保存している値をリセット
		return "index";
	}

	@GetMapping("myPage")
	public String myPage(Model model, @ModelAttribute("sessionForm") SessionForm sessionForm) {
		if (sessionForm == null || sessionForm.getUser_id() == null) {
			model.addAttribute("msg", "セッションが切れました。使用する際は再びログインしてください。");
			return "session";
		}
		// ユーザーが投稿したもの一覧表示
		List<RetentionUser> mySelfPostList = cinemasPostService.findPostsBySelf(sessionForm.getUser_id());
		model.addAttribute("mySelfPostList", mySelfPostList);
		return "myPage";
	}

	// main viewに飛んで一覧表示
	@GetMapping("main")
	public String mainView(Model model, @ModelAttribute("sessionForm") SessionForm sessionForm,
			@ModelAttribute("retentionUser") RetentionUser retentionUser) {
		if (sessionForm == null || sessionForm.getUser_id() == null) {
			model.addAttribute("msg", "セッションが切れました。使用する際は再びログインしてください。");
			return "session";
		}
		// 投稿全件取得
		List<RetentionUser> allUserPostList = cinemasPostService.findAllPost();
		// 投稿をallUserPostListに格納して全件modelに設定して画面に表示
		model.addAttribute("allUserPostList", allUserPostList);

		return "main";
	}
	
//    @GetMapping("/search")
//    public String showSearchForm(Model model) {
//        model.addAttribute("retentionUser", new RetentionUser());
//        return "main"; // 検索フォームを表示するThymeleafテンプレートの名前
//    }
    
    @GetMapping("/searchByTitle")
    public String searchByTitle(@RequestParam("title") String title, Model model) {
        List<RetentionUser> searchResults = cinemasPostService.searchByTitle(title);
        model.addAttribute("searchResults", searchResults);
        return "search"; // 適切なビュー名を指定してください
    }

	// login.thmlからsessionFomに格納され送られてきた情報を照合するメソッド
	@PostMapping("/loginPass")
	// top.htmlで値を格納したsessionFormオブジェクトを使用。()内でもう一度newさてれて値を受け取っている
	public String LoginResult(Model model, @ModelAttribute("sessionForm") SessionForm sessionForm)
			throws NoSuchAlgorithmException {

		String loginResult = null; // DBの照合結果に応じて遷移先を変えたいので、いったんnull

		// sessionFormからpassを受け取り、HashEnからのハッシュ化したパスを受け取り、sessionFormに格納。ここでハッシュ化
		sessionForm.setPassword(encorderService.encordPass(sessionForm.getPassword()));
		
		String result = accountService.loginCheck(sessionForm);
		if (result != null) {
			// sessionFormに値が入っていなかったnameフィールドをここでセット。ログインで照合してnameを引き取って、resultに格納される
			sessionForm.setUser_name(result);
			loginResult = "myPage"; // login成功→myPage.htmlへ遷移
			// ユーザーが投稿したものだけ表示
			List<RetentionUser> mySelfPostList = cinemasPostService.findPostsBySelf(sessionForm.getUser_id());
			model.addAttribute("mySelfPostList", mySelfPostList);

		} else { // resultがnullということは、DBに一致するレコードがなかったということ
			loginResult = "login"; // login失敗→login.htmlへ遷移
		}
		return loginResult;
	}

	// posting.thmlの処理
	@PostMapping("cinemaPosts")
	// Modelに格納されたRetentionUserのobjectのretentionUser keyを取得
	public String submitCinemaPost(@ModelAttribute RetentionUser retentionUser, 
            @RequestParam("rating")Integer rating, Model model,
            @ModelAttribute SessionForm sessionForm)  {
		
		// session切れの判定
		if (sessionForm == null || sessionForm.getUser_id() == null) {
			model.addAttribute("msg", "セッションが切れました。使用する際は再びログインしてください。");
			return "session";
		}
	    // ユーザーが選択した評価を取得し、RetentionUser オブジェクトにセット
	    retentionUser.setRating(rating);

	    // RetentionUser オブジェクトを使って投稿を保存する処理を追加
	    cinemasPostService.rateCinemaPost(retentionUser.getPost_id(), rating);

	    RetentionUser reUser = new RetentionUser(sessionForm.getUser_id(), retentionUser.getUser_name(), 
				retentionUser.getPost_id(), retentionUser.getTitle(), retentionUser.getImpressions(), retentionUser.getRating());
	    
		// DBに追加したい情報を格納したcinemasPostオブジェクトをサービスに渡す
		boolean result = cinemasPostService.createNewPost(reUser);
		if (result) {
			// trueの場合は投稿成功
			 model.addAttribute("message", "投稿しました。");
		} else {
			// 投稿が失敗(false)したら通るブロック
			model.addAttribute("msg", "投稿できませんでした。");
		}
		// 他の必要な処理を追加（例：データベースに保存された映画投稿を再取得）
		return "posting";
	}

	// 新規登録のチェックメソッド。register.htmlでの入力されaccountFormに格納された情報をもらう
	@PostMapping("registerCheck")
	public String registerCheck(Model model, // 新規会員登録結果を文字列をModelに格納
			@Validated AccountForm aF, BindingResult bindingResult) // register.htmlの入力フォーム内容のチェック結果が格納されている
			throws NoSuchAlgorithmException { // 例外処理
		if (bindingResult.hasErrors()) { // 何かしらの入力エラーがあった場合はtrueで再入力させる
			return "register";
		}

		// AccountForm→account entityに値を移す
		Account registerAccount = new Account(aF.getUser_id(), aF.getUser_name(), aF.getName(), aF.getPassword(),
				aF.getMail());
		registerAccount.setPassword(encorderService.encordPass(registerAccount.getPassword())); // ログイン時と同様のハッシュ化を行う

		// DBにすでに存在するか確認。trueだったら重複レコード無し、falseなら重複レコードあり
		boolean beforeCheck = accountService.registerCheck(registerAccount);
		if (!beforeCheck) { // beforeCheckの値がfalse（DBに同じレコードがあった場合）の場合にifブロック実行
			model.addAttribute("registerResult", "入力したユーザー情報は既に登録済みです。");
			return "register";
		}
		// 新規会員登録が実行できる場合は以下の処理に進む
		// 登録したい情報を格納したentityをメソッドの引数に渡してあげる
		boolean result = accountService.createNewAccount(registerAccount);
		if (result) { // レコードが一件追加できたときにこのifブロックを実行。modelのregisterResultに文字列を格納
			model.addAttribute("registerResult", "登録完了！");
		} else {
			model.addAttribute("registerResult", "登録失敗！");
		}
		return "registrationResult"; // registrationResult.htmlに飛ぶ
	}

	// 投稿を削除するメソッド
	@PostMapping("/deletePost")
	public String deletePosts(@RequestParam("selectedPosts") List<Integer> selectedPosts, Model model,
			@ModelAttribute("sessionForm") SessionForm sessionForm) {
		// 選択した投稿を削除
		for (Integer post_id : selectedPosts) {
			int deletedRows = cinemasPostService.deletePost(post_id);
			if (deletedRows == 0) {
				// 削除できなかった場合の処理
				model.addAttribute("errorMessage", "削除できませんでした。やり直してください。");
				return "myPage"; // 一覧画面にリダイレクト
			}
		}

		// 削除が成功した場合の処理
		model.addAttribute("successMessage", "選択した投稿を削除しました。");

		// 投稿を削除処理したら再表示
		List<RetentionUser> mySelfPostList = cinemasPostService.findPostsBySelf(sessionForm.getUser_id());
		model.addAttribute("mySelfPostList", mySelfPostList);
		return "myPage"; // 一覧画面にリダイレクト
	}

	// 映画投稿の評価を処理するエンドポイント
	@PostMapping("/cinemaPosts/rate/{post_id}")
	public String rateCinemaPost(@PathVariable Integer post_id, @RequestParam Integer rating) {
		// post_idに対する評価を処理し、データベースに保存することができます
		cinemasPostService.rateCinemaPost(post_id, rating);
		return "redirect:/cinemaPosts"; // 評価後に投稿一覧にリダイレクト
	}
	
    @PostMapping("/search")
    public String searchByTitle(@ModelAttribute RetentionUser retentionUser, Model model) {
        // タイトルで映画投稿を検索し、検索結果をモデルに追加する処理を追加
        List<RetentionUser> searchResults = cinemasPostService.searchByTitle(retentionUser.getTitle());
        model.addAttribute("searchResults", searchResults);
        return "main";
    }

}
