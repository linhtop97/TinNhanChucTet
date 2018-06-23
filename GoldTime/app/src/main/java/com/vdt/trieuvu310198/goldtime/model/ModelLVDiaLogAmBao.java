package com.vdt.trieuvu310198.goldtime.model;

public class ModelLVDiaLogAmBao {
    private String nameMusic;
    private boolean ischeckedMusic;

    public ModelLVDiaLogAmBao(String nameMusic, boolean ischeckedMusic) {
        this.nameMusic = nameMusic;
        this.ischeckedMusic = ischeckedMusic;
    }

    public ModelLVDiaLogAmBao() {
    }

    public void setNameMusic(String nameMusic) {
        this.nameMusic = nameMusic;
    }

    public void setIscheckedMusic(boolean ischeckedMusic) {
        this.ischeckedMusic = ischeckedMusic;
    }

    public String getNameMusic() {
        return nameMusic;
    }

    public boolean isIscheckedMusic() {
        return ischeckedMusic;
    }
}
