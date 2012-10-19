package etc;

import bumpMapping.BumpMap;
import noise.NoiseColor;

public class Effects
{
	private boolean phong;
	private boolean lambertian;
	private NoiseColor noiseMappedColorClass;
	private boolean reflective;
	private Refractive refractive;
	private BumpMap bumpMapClass;
	
	public Effects()
	{
		phong = false;
		lambertian = false;
		reflective = false;
		refractive = null;
	}
	private boolean bothLambertianAndPhong()
	{
		if(lambertian && phong)
		{
			System.err.println("Both phong and lambertian models are not allowed on the same surface.");
			System.exit(0);
		}
		return false;
	}
	public boolean isLambertian()
	{
		bothLambertianAndPhong();
		return lambertian;
	}
	public void setLambertian(boolean lambertian) {
		this.lambertian = lambertian;
	}
	public boolean isPhong() {
		bothLambertianAndPhong();
		return phong;
	}
	public void setPhong(boolean phong) {
		this.phong = phong;
	}
	public boolean isReflective() {
		return reflective;
	}
	public void setReflective(boolean reflective) {
		this.reflective = reflective;
	}
	public NoiseColor getNoiseMappedColorClass() {
		return noiseMappedColorClass;
	}
	public void setNoiseMappedColorClass(NoiseColor noiseMappedColorClass) {
		this.noiseMappedColorClass = noiseMappedColorClass;
	}
	public BumpMap getBumpMapClass()
	{
		return bumpMapClass;
	}
	public void setBumpMapClass(BumpMap bumpMapClass)
	{
		this.bumpMapClass = bumpMapClass;
	}
	public Refractive getRefractive()
	{
		return refractive;
	}
	public void setRefractive(Refractive refractive)
	{
		this.refractive = refractive;
	}
}
