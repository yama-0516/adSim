package controller;

import java.io.IOException;
import java.net.URLEncoder;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.SurveyForm;
import service.AdSuggestionService;

public class SurveyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("survey.jsp");
        dispatcher.forward(request, response);
    }

    @Override
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

        // SuggestionResult全体を渡す（重要！）
        request.setAttribute("suggestionResult", result);

        // URLエンコード済みの広告タイプを渡す（必要なら）
        String encodedAdType = URLEncoder.encode(result.getMediaId(), "UTF-8");
        request.setAttribute("encodedAdType", encodedAdType);

        // 結果画面へフォワード
        RequestDispatcher dispatcher = request.getRequestDispatcher("result.jsp");
        dispatcher.forward(request, response);
        
        
    }
    
 }