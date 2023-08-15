package etc;

import etc.mapper.ImageMapper;
import bumpMapping.BumpMap;
import noise.NoiseColor;

public class Effects
{
	private Phong phong;
	private boolean lambertian;
	private NoiseColor noiseMappedColorClass;
	private boolean reflective;
	private Refractive refractive;
	private BumpMap bumpMapClass;
	private ImageMapper imageMapper;
	
	public Effects()
	{
		phong = null;
		lambertian = false;
		reflective = false;
		refractive = null;
	}
	private boolean bothLambertianAndPhong() throws RaytracerException
	{
		if(lambertian && phong != null)
		{
			throw new RaytracerException("Both phong and lambertian models are not allowed on the same surface.");
		}
		return false;
	}
	public boolean isLambertian() throws RaytracerException
	{
		bothLambertianAndPhong();
		return lambertian;
	}
	public void setLambertian(boolean lambertian) {
		this.lambertian = lambertian;
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
	public Phong getPhong()
	{
		return phong;
	}
	public void setPhong(Phong phong)
	{
		this.phong = phong;
	}
	public ImageMapper getImageMapper()
	{
		return imageMapper;
	}
	public void setImageMapper(ImageMapper imageMapper)
	{
		this.imageMapper = imageMapper;
	}
}
