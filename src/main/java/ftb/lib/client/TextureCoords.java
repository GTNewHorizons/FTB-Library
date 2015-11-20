package ftb.lib.client;

import latmod.lib.LMUtils;
import net.minecraft.util.ResourceLocation;

public final class TextureCoords
{
	public static final TextureCoords nullTexture = new TextureCoords(null, 0, 0, 0, 0, 0, 0);
	
	public final ResourceLocation texture;
	public final double posX, posY, width, height;
	public final int posXI, posYI, widthI, heightI;
	public final double textureW, textureH;
	public final double minU, minV, maxU, maxV;
	
	private String toString = null;
	
	public TextureCoords(ResourceLocation res, double x, double y, double w, double h, double tw, double th)
	{
		texture = res;
		posX = x;
		posY = y;
		width = w;
		height = h;
		posXI = (int)posX;
		posYI = (int)posY;
		widthI = (int)width;
		heightI = (int)height;
		
		textureW = tw;
		textureH = th;
		
		minU = posX / textureW;
		minV = posY / textureH;
		maxU = (posX + width) / textureW;
		maxV = (posY + height) / textureH;
	}
	
	public TextureCoords(ResourceLocation res, int x, int y, int w, int h)
	{ this(res, x, y, w, h, 256, 256); }
	
	public int hashCode()
	{ return LMUtils.hashCode(texture, posX, posY, width, height); }
	
	public String toString()
	{
		if(toString == null)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(posXI);
			sb.append(',');
			sb.append(posYI);
			sb.append(',');
			sb.append(widthI);
			sb.append(',');
			sb.append(heightI);
			toString = sb.toString();
		}
		
		return toString;
	}
	
	public int getWidth(double h)
	{ return (int)(width * (h / (double)height)); }
	
	public int getHeight(double w)
	{ return (int)(height * (w / (double)width)); }
	
	public boolean isValid()
	{ return texture != null && width > 0 && height > 0; }
	
	public TextureCoords clone()
	{ return new TextureCoords(texture, posX, posY, width, height, textureW, textureH); }
	
	public TextureCoords[] split(int x, int y)
	{
		if(x == 0 || y == 0) return new TextureCoords[0];
		if(x == 1 && y == 1) return new TextureCoords[] { clone() };
		if(x == 1) return splitY(y);
		if(y == 1) return splitX(x);
		
		TextureCoords[] l = new TextureCoords[x * y];
		TextureCoords[] ly = splitY(y);
		
		for(int y1 = 0; y1 < y; y1++)
		{
			l[y1 * x] = ly[y1];
			TextureCoords[] lx = ly[y1].splitX(x);
			for(int x1 = 0; x1 < x; x1++)
				l[y1 * x + x1] = lx[x1];
		}
		
		return l;
	}
	
	private TextureCoords[] splitX(int s)
	{
		TextureCoords[] l = new TextureCoords[s];
		double ds = (double)s;
		double d = width / ds;
		for(int i = 0; i < s; i++)
			l[i] = new TextureCoords(texture, posX + d * i, posY, d, height, textureW, textureH);
		return l;
	}
	
	private TextureCoords[] splitY(int s)
	{
		TextureCoords[] l = new TextureCoords[s];
		double ds = (double)s;
		double d = height / ds;
		for(int i = 0; i < s; i++)
			l[i] = new TextureCoords(texture, posX, posY + d * i, width, d, textureW, textureH);
		return l;
	}
}