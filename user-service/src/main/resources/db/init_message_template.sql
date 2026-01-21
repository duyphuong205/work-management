create table message_template (
    template_id bigint not null auto_increment,
    type varchar(20),
    title nvarchar(200),
    content mediumtext,
    status varchar(4),
    language varchar(5),
    created_by bigint,
    created_time timestamp,
    updated_by bigint,
    updated_time timestamp,
    primary key(template_id)
);

--Mail xác thực khi đăng ký người dùng
INSERT INTO message_template
(template_id, type, title, content, status, language, created_by, created_time, updated_by, updated_time)
VALUES(1, 'UG001', N'Mã xác minh đăng ký tài khoản', '<body style="margin:0; padding:0; background:#f3f5f7; font-family: Arial, Helvetica, sans-serif;">
    <table role="presentation" width="100%" cellspacing="0" cellpadding="0" border="0"
        style="background:#f3f5f7; padding:24px 0;">
        <tr>
            <td align="center" style="padding:0 12px;">
                <table role="presentation" width="600" cellspacing="0" cellpadding="0" border="0"
                    style="max-width:600px; width:100%; background:#ffffff; border:1px solid #e6e8eb; border-radius:12px; overflow:hidden;">
                    <tr>
                        <td style="padding:20px 24px; border-bottom:1px solid #eef0f2;">
                            <table role="presentation" width="100%" cellspacing="0" cellpadding="0" border="0">
                                <tr>
                                    <td style="font-size:20px; font-weight:700; color:#222;">
                                        Mã Xác Minh Đăng Ký Tài Khoản
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td style="padding:24px;">
                            <p style="margin:0 0 12px; font-size:16px; color:#111827;">Chào %fullName%,</p>
                            <p style="margin:0 0 18px; font-size:15px; line-height:1.6; color:#374151;">
                                Đây là mã OTP để đăng ký tài khoản của bạn. Vui lòng nhập mã dưới đây để hoàn tất xác
                                minh.
                            </p>
                            <table role="presentation" cellspacing="0" cellpadding="0" border="0" width="100%"
                                style="margin:18px 0 10px;">
                                <tr>
                                    <td align="center">
                                        <div
                                            style="display:inline-block; padding:18px 22px; border:2px solid #2563eb; border-radius:12px;">
                                            <div
                                                style="font-size:14px; font-weight:700; color:#1f3b74; margin-bottom:8px;">
                                                Mã Xác Minh Của Bạn
                                            </div>
                                            <div
                                                style="font-size:44px; letter-spacing:10px; font-weight:800; color:#0f2e66; line-height:1;">
                                                %otpNum%
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                            <p style="margin:10px 0 0; font-size:14px; line-height:1.6; color:#374151;">
                                Mã này có hiệu lực trong <strong>%otpExpireMinutes% phút</strong>.
                            </p>
                            <div style="height:1px; background:#eef0f2; margin:20px 0;"></div>

                            <p style="margin:0; font-size:13px; line-height:1.6; color:#6b7280;">
                                Nếu bạn không yêu cầu đăng ký tài khoản, vui lòng bỏ qua email này.
                            </p>
                        </td>
                    </tr>
                </table>
                <div style="max-width:600px; width:100%; font-size:0; line-height:0;">&nbsp;</div>
            </td>
        </tr>
    </table>
</body>
', 'ACTV', 'vi_VN', -1, CURRENT_TIMESTAMP, NULL, NULL);