package service;

import model.SurveyForm;



public class AdSuggestionService {
    public static class SuggestionResult {
        private String suggestion;         // おすすめ媒体
        private String reason;             // なぜおすすめするかの理由・解説
        private String evidenceUrl;        // 根拠となるデータやソースURL

        public SuggestionResult(String suggestion, String reason, String evidenceUrl) {
            this.suggestion = suggestion;
            this.reason = reason;
            this.evidenceUrl = evidenceUrl;
        }

        public String getSuggestion() { return suggestion; }
        public String getReason() { return reason; }
        public String getEvidenceUrl() { return evidenceUrl; }
    }

    public SuggestionResult suggest(SurveyForm form) {
        String age = form.getAgeGroup();
        String gender = form.getGender();
        String interest = form.getInterest();


        if ("10代".equals(age) || "エンタメ".equals(interest)) {
            return new SuggestionResult(
                "YouTube広告やInstagram広告が効果的です。",
                "10代やエンタメに関心がある層は動画やSNSの利用率が非常に高く、YouTubeやInstagramの広告が届きやすいためおすすめです。",
                "https://www.soumu.go.jp/johotsusintokei/statistics/statistics05.html" // 総務省 情報通信メディアの利用時間と情報行動に関する調査
            );
        }

        if ("女性".equals(gender) && "ファッション".equals(interest)) {
            return new SuggestionResult(
                "Instagram広告やLINE広告が適しています。",
                "女性のファッション関心層はInstagramやLINEを利用する割合が高く、視覚的な訴求が有効です。",
                "https://www.statista.com/statistics/972939/japan-instagram-user-gender/" // Statista Instagram demographics Japan
            );
        }

        if ("ビジネス".equals(interest)) {
            return new SuggestionResult(
                "YouTube広告やLinkedIn広告がオススメです。",
                "ビジネス関心層はYouTubeでビジネス系動画やLinkedInで情報収集する傾向が強いため、両媒体が有効です。",
                "https://jp.linkedin.com/company/linkedin-japan" // LinkedIn公式
            );
        }

        if ("60代以上".equals(age)) {
            return new SuggestionResult(
                "LINE広告やFacebook広告が効果的です。",
                "60代以上はLINEやFacebookの利用率が高く、スマートフォンでの情報収集が主流となっています。",
                "https://www.nri.com/jp/knowledge/blog/lst/2022/fis/kiuchi/1222" // NRI シニア層のSNS利用
            );
        }

        return new SuggestionResult(
            "YouTube広告、LINE広告の組み合わせをおすすめします。",
            "幅広い年齢層にリーチできるYouTubeとLINEの併用は、ターゲットが明確でない場合でも効果が期待できます。",
            "https://www.dentsu.co.jp/news/release/2023/0517-010669.html" // 電通メディアイノベーションラボ SNSユーザー調査
        );
    }
}