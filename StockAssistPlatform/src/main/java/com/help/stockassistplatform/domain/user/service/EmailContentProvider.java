package com.help.stockassistplatform.domain.user.service;

import org.springframework.stereotype.Component;

@Component
class EmailContentProvider {
	String buildEmailSubject() {
		return "📢 투자인 회원가입 이메일 인증";
	}

	String buildEmailContent(final String verificationLink) {
		return "<!DOCTYPE html>" +
			"<html lang='ko'>" +
			"<head>" +
			"    <meta charset='UTF-8'>" +
			"    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
			"    <title>투자인 회원가입 이메일 인증</title>" +
			"    <style>" +
			"        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; text-align: center; }"
			+
			"        .container { max-width: 600px; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); margin: auto; }"
			+
			"        h2 { color: #333; }" +
			"        p { color: #666; font-size: 16px; }" +
			"        .button { display: inline-block; padding: 10px 20px; color: white; background-color: #007bff; border-radius: 5px; text-decoration: none; font-weight: bold; }"
			+
			"        .button:hover { background-color: #0056b3; }" +
			"        footer { margin-top: 20px; font-size: 12px; color: #999; }" +
			"    </style>" +
			"</head>" +
			"<body>" +
			"    <div class='container'>" +
			"        <h2>📢 투자인 회원가입 이메일 인증</h2>" +
			"        <p>안녕하세요, 투자인입니다!</p>" +
			"        <p>아래 버튼을 클릭하여 이메일 인증을 완료하세요.</p>" +
			"        <a class='button' href='" + verificationLink + "'>이메일 인증하기</a>" +
			"        <p>또는 아래 링크를 직접 복사하여 브라우저에서 여세요.</p>" +
			"        <p><a href='" + verificationLink + "'>" + verificationLink + "</a></p>" +
			"        <footer>이 메일은 자동 발송된 메일이며, 회신할 수 없습니다.</footer>" +
			"    </div>" +
			"</body>" +
			"</html>";
	}
}
