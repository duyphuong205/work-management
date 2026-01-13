package com.cloud.work.enums;

public enum Status {
    NEWR, //Tạo mới (new record)
    SMTM, //Timeout (submit timeout)
    ACTV, //Hoạt động (active)
    INTV, //Không hoạt động (inactive)
    FRZU, //Tạm ngưng,đóng băng (freeze up)
    DLTD, //Đã xoá (deleted)
    SECP, //Chờ kích hoạt (pending)
    EXPD, //Hết hạn (expired)
    EXPS, //Sắp hết hạn (Expiring soon)
    LOCK, //Khoá (lock)
    SUCC, //Thành công (success)
    FAIL, //Thất bại (fail)
    PTSM, //Chờ xử lý (pending to submit)
    PTCP, //Chờ hoàn thành (pending to complete)
    PTFH, //Đang chờ kết thúc (pending to finish)
    CKRJ, //Từ chối (rejected)
}
