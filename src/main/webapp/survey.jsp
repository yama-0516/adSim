<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>広告媒体診断</title>
    <link rel="stylesheet" href="style.css">
   <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<div class="container">
    <h1>広告媒体 診断フォーム</h1>
    <form action="Survey" method="post">
        <label for="ageGroup">年齢層</label>
        <select id="ageGroup" name="ageGroup" required>
            <option value="">選択してください</option>
            <option value="10代">10代</option>
            <option value="20代">20代</option>
            <option value="30代">30代</option>
            <option value="40代">40代</option>
            <option value="50代">50代</option>
            <option value="60代以上">60代以上</option>
        </select>
        <label for="gender">性別</label>
        <select id="gender" name="gender" required>
            <option value="">選択してください</option>
            <option value="男性">男性</option>
            <option value="女性">女性</option>
            <option value="どちらも">どちらも</option>
        </select>
        <label for="interest">興味・関心</label>
        <select id="interest" name="interest" required>
            <option value="">選択してください</option>
            <option value="ファッション">ファッション</option>
            <option value="エンタメ">エンタメ</option>
            <option value="ビジネス">ビジネス</option>
            <option value="スポーツ">スポーツ</option>
            <option value="グルメ">グルメ</option>
            <option value="旅行">旅行</option>
            <option value="IT・テクノロジー">IT・テクノロジー</option>
            <option value="健康・美容">健康・美容</option>
            <option value="その他">その他</option>
        </select>
        <input type="submit" value="診断する">
    </form>
</div>
</body>
</html>