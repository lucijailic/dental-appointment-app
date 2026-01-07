package ba.sum.fsre.dentalappointemntapp.data.repository;

public interface RepositoryCallback<T> {
    void onSuccess(T data);
    void onError(String error);
}
