package edu.abdullah.final_project.dtos;

public class ChangePasswordDTO {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    public boolean passwordsMatch() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }

    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}