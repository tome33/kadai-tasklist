package controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import models.validators.MessageValidator;
import util.DBUtil;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // CSR対策（_token に値がセットされていなかったりセッションIDと値が異なったりしたらデータの登録ができないようにしています。）
        String _token = request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            // モデルのインスタンス化（フォームに入力されたタスク、登録した日時）
            Task tsk = new Task();

            String content = request.getParameter("content");
            tsk.setContent(content);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            tsk.setCreated_at(currentTime);
            tsk.setUpdated_at(currentTime);

            // 入力内容のチェック（バリデーション）を実行してエラーがあったら新規登録のフォームに戻る
            List<String> errors = MessageValidator.validate(tsk);
            if(errors.size() > 0) {
                em.close();

                // フォームに初期値を設定、さらにエラーメッセージを送る
                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("task", tsk);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasklist/new.jsp");
                rd.forward(request, response);
            } else {
            // エンティティマネージャ（データベースに保存）
            em.getTransaction().begin();		// トランザクションの開始
            em.persist(tsk);					// データベースに上のインスタンス化のデータを保存する
            em.getTransaction().commit();		// コミット（トランザクションの処理を一括実行）
            em.close();							// エンティティマネージャを開放して作業終了

            // インデックスのページへ推移
            response.sendRedirect(request.getContextPath() + "/index");
            }
        }
    }

}
