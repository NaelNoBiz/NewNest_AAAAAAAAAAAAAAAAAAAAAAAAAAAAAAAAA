package com.example.newnest;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseServices {

    private static FirebaseServices instance;
    private FirebaseAuth auth;
    private FirebaseFirestore fire;
    private FirebaseStorage storage;

    public FirebaseServices() {
        auth= FirebaseAuth.getInstance();
        fire= FirebaseFirestore.getInstance();
        storage= FirebaseStorage.getInstance();
    }

    public static FirebaseServices getInstance() {
        return instance;
    }

    public static void setInstance(FirebaseServices instance) {
        FirebaseServices.instance = instance;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }

    public FirebaseFirestore getFire() {
        return fire;
    }

    public void setFire(FirebaseFirestore fire) {
        this.fire = fire;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public void setStorage(FirebaseStorage storage) {
        this.storage = storage;
    }
}
