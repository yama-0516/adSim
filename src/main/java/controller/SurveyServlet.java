package controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.SurveyForm;
import service.AdSuggestionService;

@WebServlet("/Survey")
public class SurveyServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("survey.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	 request.setCharacterEncoding("UTF-8");

        // フォームデータ取得
        String ageGroup = request.getParameter("ageGroup");
        String gender = request.getParameter("gender");
        String interest = request.getParameter("interest");

        // モデル作成
        SurveyForm form = new SurveyForm();
        form.setAgeGroup(ageGroup);
        form.setGender(gender);
        form.setInterest(interest);

        // サービス呼び出し
        AdSuggestionService service = new AdSuggestionService();
        AdSuggestionService.SuggestionResult result = service.suggest(form);
        request.setAttribute("result", result);

        // 結果画面へフォワード
        RequestDispatcher dispatcher = request.getRequestDispatcher("result.jsp");
        dispatcher.forward(request, response);
    }
}