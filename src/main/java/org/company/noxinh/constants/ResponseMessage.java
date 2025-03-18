package org.company.noxinh.constants;

import lombok.Getter;

/**
 * The enum Response message.
 */
@Getter
public enum ResponseMessage {
    /* ---------------------------------- LIST OF SUCCESS ---------------------------------- */

    /**
     * The S 001.
     */
    S001("Đăng nhập thành công"),
    /**
     * The S 002.
     */
    S002("Lấy danh sách province thành công"),
    /**
     * The S 003.
     */
    S003("Lấy danh sách district thành công"),
    /**
     * The S 004.
     */
    S004("Lấy danh sách ward thành công"),
    /**
     * The S 005.
     */
    S005("Lấy danh sách account thành công"),
    /**
     * The S 006.
     */
    S006("Đã tìm thấy user"),
    /**
     * The S 007.
     */
    S007("Đổi mật khẩu thành công"),
    /**
     * The S 008.
     */
    S008("Đăng ký tài khoản thành công"),
    /**
     * The S 009.
     */
    S009("Đã cập nhật tài khoản thành công"),
    /**
     * The S 010.
     */
    S010("Đã thay đổi mật khẩu tài khoản thành công"),

    /**
     * The S 011.
     */
    S011("Tạo loại sản phẩm thành công"),
    /**
     * The S 012.
     */
    S012("Lấy danh sách loại sản phẩm thành công"),
    /**
     * The S 013.
     */
    S013("Cập nhật loại sản phẩm thành công"),
    /**
     * The S 014.
     */
    S014("Đã thêm sản phẩm vào danh mục thành công"),
    /**
     * The S 015.
     */
    S015("Tạo banner thành công"),
    /**
     * The S 016.
     */
    S016("Thay đổi hiển thị banner thành công"),
    /**
     * The S 017.
     */
    S017("Hiển thị banner thành công"),
    /**
     * The S 018.
     */
    S018("Xoá banner thành công"),
    /**
     * The S 019.
     */
    S019("Cập nhật banner thành công"),
    /**
     * The S 020.
     */
    S020("Cập nhật vị trí hiển thị banner thành công"),
    /**
     * The S 021.
     */
    S021("Đã gửi OTP vào Email. Xin vui lòng kiểm tra"),
    /**
     * The S 022.
     */
    S022("Thay đổi mật khẩu thành công"),
    /**
     * The S 023.
     */
    S023("Làm mới token thành công"),






    /* ---------------------------------- LIST OF ERROR ---------------------------------- */

    /**
     * The E 001.
     */
    E001("Người dùng không được tìm thấy !"),
    /**
     * The E 002.
     */
    E002("Tên đăng nhập hoặc mật khẩu không đúng !"),
    /**
     * The E 003.
     */
    E003("Sai format !"),
    /**
     * The E 004.
     */
    E004("Request body trống !"),
    /**
     * The E 005.
     */
    E005("Lấy ward thất bại !"),
    /**
     * The E 006.
     */
    E006("Lấy district thất bại !"),
    /**
     * The E 007.
     */
    E007("Lấy province thất bại !"),
    /**
     * The E 008.
     */
    E008("Xảy ra lỗi khi tìm user!"),
    /**
     * The E 009.
     */
    E009("Sai mật khẩu"),
    /**
     * The E 010.
     */
    E010("Mật khẩu không giống nhau"),
    /**
     * The E 011.
     */
    E011("Không được nhập mật khẩu cũ"),
    /**
     * The E 012.
     */
    E012("Xuất hiện lỗi khi đổi mật khẩu"),
    /**
     * The E 013.
     */
    E013("Không tìm thấy user"),
    /**
     * The E 014.
     */
    E014("Xuất hiện lỗi khi đăng ký tài khoản"),

    /**
     * The E 015.
     */
    E015("Số điện thoại đã có người sử dụng"),
    /**
     * The E 016.
     */
    E016("Xảy ra lỗi khi cập nhật tài khoản"),
    /**
     * The E 017.
     */
    E017("Xảy ra lỗi khi thay đổi password tài khoản"),
    /**
     * The E 018.
     */
    E018("Loại sản phẩm đã tồn tại trong hệ thống"),

    /**
     * The E 019.
     */
    E019("Xảy ra lỗi khi tạo loại sản phẩm"),
    /**
     * The E 020.
     */
    E020("Không tìm thấy loại sản phẩm"),
    /**
     * The E 021.
     */
    E021("Lỗi cập nhật loại sản phẩm"),
    /**
     * The E 022.
     */
    E022("Không tìm thấy sản phẩm"),
    /**
     * The E 023.
     */
    E023("Sản phẩm đang sử dụng trong danh mục"),
    /**
     * The E 024.
     */
    E024("Lỗi khi thêm sản phẩm trong danh mục"),
    /**
     * The E 025.
     */
    E025("Lỗi khi tạo banner"),
    /**
     * The E 026.
     */
    E026("Không tìm thấy banner ID: "),
    /**
     * The E 027.
     */
    E027("Lỗi thay đổi display banner "),
    /**
     * The E 028.
     */
    E028("Không tìm thấy banner "),
    /**
     * The E 029.
     */
    E029("Lỗi xoá banner "),
    /**
     * The E 030.
     */
    E030("Không tìm thấy banner hiển thị"),
    /**
     * The E 031.
     */
    E031("Tổng số hiển thị banner vượt quá 3 nên không thể hiển thị thêm"),
    /**
     * The E 032.
     */
    E032("Bạn đã gửi quá nhiều yêu cầu. Vui lòng thử lại sau."),
    /**
     * The E 033.
     */
    E033("Không thấy tài khoản khớp với email trong hệ thống "),
    /**
     * The E 034.
     */
    E034("Lỗi gửi OTP đến email "),
    /**
     * The E 035.
     */
    E035("OTP không tồn tại"),
    /**
     * The E 036.
     */
    E036("OTP không hợp lệ"),
    /**
     * The E 037.
     */
    E037("UUID không hợp lệ"),
    /**
     * The E 038.
     */
    E038("OTP hết hạn"),
    /**
     * The E 039.
     */
    E039("Xuất hiện lỗi khi reset password tài khoản"),
    /**
     * The E 040.
     */
    E040("Xuất hiện lỗi khi làm mới token");


    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }
}
