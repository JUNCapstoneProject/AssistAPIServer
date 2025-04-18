package com.help.stockassistplatform.domain.user.service;

import org.springframework.stereotype.Component;

@Component
class EmailContentProvider {
	String buildEmailSubject() {
		return "📢 투자인 회원가입 이메일 인증";
	}

	String buildEmailContent(final String verificationLink) {
		final StringBuilder builder = new StringBuilder();

		builder.append("<!DOCTYPE html>")
			.append("<html lang='ko'>")
			.append("<head>")
			.append("    <meta charset='UTF-8'>")
			.append("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>")
			.append("    <title>이메일 인증</title>")
			.append("</head>")
			.append(
				"<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; text-align: center;'>")
			.append("    <div style='max-width: 600px; background: #ffffff; padding: 20px; border-radius: 8px; ")
			.append("box-shadow: 0 0 10px rgba(0,0,0,0.1); margin: 40px auto; text-align: center;'>")
			.append("        <div style='background: #003366; color: white; padding: 20px; font-size: 24px; ")
			.append("text-align: left;'><strong>투자인</strong>에 <strong>가입</strong>해주셔서 감사합니다.</div>")
			.append("        <div style='padding: 20px; font-size: 16px; color: #333; text-align: center;'>")
			.append("            <p>투자인에 등록한 메일주소가 올바른지 확인하기 위한 메일입니다.</p>")
			.append("            <p>메일등록을 완료하시려면 아래 버튼을 눌러주세요.</p>")
			.append("            <a href='").append(verificationLink)
			.append("' style='display: inline-block; padding: 12px 24px; margin: 20px auto; color: white; ")
			.append("background-color: #007bff; border-radius: 5px; text-decoration: none; font-weight: bold;'>")
			.append("이메일 인증하기</a>")
			.append("        </div>")
			.append("        <div style='margin-top: 20px; font-size: 12px; color: #999; text-align: center;'>")
			.append("이 메일은 자동 발송된 메일이며, 회신할 수 없습니다.</div>")
			.append("    </div>")
			.append("</body>")
			.append("</html>");

		return builder.toString();
	}
}
