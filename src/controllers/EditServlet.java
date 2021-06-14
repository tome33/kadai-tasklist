package controllers;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Task;
import util.DBUtil;



/**
 * Servlet implementation class EditServlet
 */
@WebServlet("/edit")
public class EditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        // 該当のID(一覧ページでクリックされたID)のタスク1件のみをデータベースから取得
        // request.getParameter() は文字データとして取得するので、Interger.parseInt() メソッドで整数値に置き換える
        Task tsk = em.find(Task.class, Integer.parseInt(request.getParameter("id")));

        em.close();

        // タスク内容情報とセッションIDをリクエストスコープに登録
        request.setAttribute("task", tsk);
        request.setAttribute("_token", request.getSession().getId());

        // タスクデータが存在しているときのみ
        // タスクIDをセッションスコープに登録
        if(tsk != null) {
            request.getSession().setAttribute("task_id", tsk.getId());
        }

        // 編集ページへ推移
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/edit.jsp");
        rd.forward(request, response);
    }

}
