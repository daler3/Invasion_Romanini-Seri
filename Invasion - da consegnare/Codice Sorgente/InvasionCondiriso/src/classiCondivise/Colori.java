package classiCondivise;

/**
 * 
 * Enum dei colori, con relativo rgb.
 *
 */

public enum Colori {
	GIALLO(255,255,0, "Giallo", false),
	ROSSO(255,0,0, "Rosso", true),
	VERDE(0,255,0, "Verde", false),
	BLU(0,0,255, "Blu", true),
	VIOLA(143,0,255, "Viola", true),
	NERO(0,0,0, "Nero", true),
	NEUTRO(255,255,255, "Neutro", false);
	
	private Integer r;
	private Integer g;
	private Integer b;
	private String nome;
	private boolean foregroundBianco;
	
	private Colori(Integer r, Integer g, Integer b, String nome, boolean valoreForeground) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.nome = nome;
		this.foregroundBianco = valoreForeground;
	}
	
	public Integer getR() {
		return r;
	}
	public Integer getG() {
		return g;
	}
	public Integer getB() {
		return b;
	}
	public String getNome(){
		return nome;
	}
	public boolean getForeground() {
		return foregroundBianco;
	}
}
