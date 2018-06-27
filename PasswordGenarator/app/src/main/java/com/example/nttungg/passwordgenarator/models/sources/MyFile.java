package com.example.nttungg.passwordgenarator.models.sources;

import android.content.Context;
import android.util.Log;

import com.example.nttungg.passwordgenarator.models.UserData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by nttungg on 6/18/18.
 */

public class MyFile implements DataSource{
    private Context mContext;
    private ArrayList<UserData> accounts = new ArrayList<>();
    private java.io.File encryptedFile;
    private java.io.File decryptedFile;
    private String key = "Mary has one cat";
    private CryptoUtils mCryptoUltils;

    public File hideFile(java.io.File file){
        java.io.File dstFile = new java.io.File(file.getParent(), "." + file.getName());
        file.renameTo(dstFile);
        return dstFile;
    }

    public MyFile(Context mContext) {
        this.mContext = mContext;
        encryptedFile =  new java.io.File(mContext.getFilesDir(),"document.encrypted");
        decryptedFile = new java.io.File(mContext.getFilesDir(),"document.decrypted");
        mCryptoUltils = new CryptoUtils(mContext);
    }

    @Override
    public void saveDataDefault(Callback callback, Context context, String account, String password, String catogory, String title) {
        readData(context, callback,false);
        try {
            FileOutputStream fos = context.openFileOutput(decryptedFile.getName(),Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            UserData newaccount = new UserData(title,account, password,catogory);
            accounts.add(newaccount);
            oos.writeObject(accounts);
            callback.onWriteSucess();
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mCryptoUltils.encrypt(key,decryptedFile, encryptedFile);
        } catch (CryptoException ex) {
            ex.getMessage();
        }
        hideFile(encryptedFile);
        decryptedFile.delete();
    }

    @Override
    public void editData(Callback callback, Context context, String account, String password, String catogory, String title, int i) {
        readData(context, callback,false);
        try {
            FileOutputStream fos = context.openFileOutput(decryptedFile.getName(),Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            accounts.get(i).setAccount(account);
            accounts.get(i).setTitle(title);
            accounts.get(i).setCatogory(catogory);
            accounts.get(i).setPassword(password);
            oos.writeObject(accounts);
            callback.onWriteSucess();
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mCryptoUltils.encrypt(key,decryptedFile, encryptedFile);
        } catch (CryptoException ex) {
            ex.getMessage();
        }
        hideFile(encryptedFile);
        decryptedFile.delete();
    }

    @Override
    public void deleteData(Callback callback,Context context, int i) {
        readData(context, callback,false);
        try {
            FileOutputStream fos = context.openFileOutput(decryptedFile.getName(),Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            accounts.remove(i);
            oos.writeObject(accounts);
            callback.onGetSuccess(accounts);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mCryptoUltils.encrypt(key,decryptedFile, encryptedFile);
        } catch (CryptoException ex) {
            ex.getMessage();
        }
        hideFile(encryptedFile);
        decryptedFile.delete();
    }

    @Override
    public void readData(Context context, Callback<ArrayList<UserData>> callback,boolean isRemove) {
        try {
            mCryptoUltils.decrypt(key, hideFile(encryptedFile),decryptedFile);
        } catch (CryptoException ex) {
            ex.getMessage();
            callback.onGetFailure(ex.getMessage());
        }
        try {
            FileInputStream fis = context.openFileInput(decryptedFile.getName());
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = null;
            accounts = new ArrayList<>();
            accounts = (ArrayList<UserData>) ois.readObject();
            callback.onGetSuccess(accounts);
            ois.close();
            fis.close();
        } catch (Exception e) {

        }
        if (isRemove){
            decryptedFile.delete();
        }
    }
}
