package tlu.edu.vn.ht63.notifaction.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import tlu.edu.vn.ht63.notifaction.Model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageRepo {
    // MutableLiveData giúp cập nhật dữ liệu
    private static MutableLiveData<List<Message>> msgListLiveData = new MutableLiveData<>(new ArrayList<>());

    // Hàm thêm thông báo vào danh sách
    public static void addMessage(Message msg) {
        List<Message> currentList = msgListLiveData.getValue();
        if (currentList != null) {
            currentList.add(msg);
            msgListLiveData.setValue(currentList);
        }
    }
    public static void removeMessage(){
        List<Message> currentList = msgListLiveData.getValue();
        if (currentList != null) {
            currentList.remove(0);
            msgListLiveData.postValue(currentList); // Sử dụng postValue thay vì setValue
        }
    }
    public static LiveData<List<Message>> getMsgList() {
        return msgListLiveData;
    }
}
