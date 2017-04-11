package interfaces;

import model.Page;

public interface IPage<T> {
	public Page<T> get();
	public Page<T> get(int pageNumero, int length);
}