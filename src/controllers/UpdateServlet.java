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
import models.validators.TaskValidator;
import util.DBUtil;



/**
 * Servlet implementation class UpdateServlet
 */
@WebServlet("/update")
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateServlet() {
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

            // セッションスコープからメッセージのIDを取得して
            // 該当のIDのメッセージ1件のみをデータベースから取得
            Task tsk = em.find(Task.class, (Integer)(request.getSession().getAttribute("task_id")));

            // フォームの内容を各フィールドに上書き

            String content = request.getParameter("content");
            tsk.setContent(content);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            tsk.setUpdated_at(currentTime);       // 更新日時のみ上書き

            // バリデーションを実行してエラーがあったら編集画面のフォームに戻る
            List<String> errors = TaskValidator.validate(tsk);
            if(errors.size() > 0) {
                em.close();

                // フォームに初期値を設定、さらにエラーメッセージを送る
                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("task", tsk);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/edit.jsp");
                rd.forward(request, response);
            } else {
                // データベースを更新
                em.getTransaction().begin();		// トランザクションの開始
                em.getTransaction().commit();

                // フラッシュメッセージをセッションスコープに保存する
                request.getSession().setAttribute("flush", "更新が完了しました。");

                em.close();							// エンティティマネージャを開放して作業終了

                // セッションスコープ上の不要になったデータを削除
                request.getSession().removeAttribute("task_id");

                // indexページへリダイレクト
                response.sendRedirect(request.getContextPath() + "/index");
            }
        }
    }
}