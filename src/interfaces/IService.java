package interfaces;

public interface IService<T> {
	
	public T get(int id);
	
	public boolean add(T object);
	
	public boolean update(int id, T object);
	
	public boolean delete(int id);
}
