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
 * Servlet implementation class ShowServlet
 */
@WebServlet("/show")
public class ShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowServlet() {
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

        // タスクデータをリクエストスコープにセットしてshow.jspを呼び出す
        request.setAttribute("task", tsk);

        RequestDispatcher rd =request.getRequestDispatcher("/WEB-INF/views/tasks/show.jsp");
        rd.forward(request, response);

    }

}
