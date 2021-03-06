package powercrystals.minefactoryreloaded.render.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.render.model.RedNetCardsModel;
import powercrystals.minefactoryreloaded.render.model.RedNetLogicModel;
import powercrystals.minefactoryreloaded.tile.rednet.TileEntityRedNetLogic;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RedNetLogicRenderer extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler
{
	private static final ResourceLocation redcomp = new ResourceLocation(MineFactoryReloadedCore.tileEntityFolder + "redcomp.png");
	private RedNetLogicModel _logicModel = new RedNetLogicModel();
	private RedNetCardsModel _cardsModel = new RedNetCardsModel();
	
	public RedNetLogicRenderer()
	{
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		bindTexture(redcomp);
		
		GL11.glPushMatrix();
		GL11.glTranslated(0.12, 0, 0);
		_logicModel.render(0.0625f);
		GL11.glPopMatrix();
		
		return;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		return false;
	}
	
	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}
	
	@Override
	public int getRenderId()
	{
		return MineFactoryReloadedCore.renderIdRedNetLogic;
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{
		TileEntityRedNetLogic logic = (TileEntityRedNetLogic) tileentity;
		int rotation = tileentity.getWorldObj().getBlockMetadata(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
		bindTexture(redcomp);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5f, (float) z + 0.5F);
		
		// Techne sucks so do some crazy rotations to turn techne coords into
		// real coords
		GL11.glRotatef(180, 0, 0, 1);
		GL11.glRotatef(180 + (90 * rotation), 0, 1, 0);
		
		// Manually translate and then render each slot with the cards texture
		// up
		bindTexture(RedNetCardItemRenderer.cards);
		GL11.glTranslatef(-0.4375f, -0.375f, -0.390625f);
		renderCard(logic.getLevelForSlot(0));
		
		GL11.glTranslatef(0, 0, 0.234375f);
		renderCard(logic.getLevelForSlot(1));
		
		GL11.glTranslatef(0, 0, 0.234375f);
		renderCard(logic.getLevelForSlot(2));
		
		GL11.glTranslatef(0, 0.375f, -0.46875f);
		renderCard(logic.getLevelForSlot(3));
		
		GL11.glTranslatef(0, 0, 0.234375f);
		renderCard(logic.getLevelForSlot(4));
		
		GL11.glTranslatef(0, 0, 0.234375f);
		renderCard(logic.getLevelForSlot(5));
		
		GL11.glPopMatrix();
	}
	
	private void renderCard(int cardLevel)
	{
		switch(cardLevel)
		{
		case 1:
			_cardsModel.renderLevel1(0.0625f);
			break;
		case 2:
			_cardsModel.renderLevel2(0.0625f);
			break;
		case 3:
			_cardsModel.renderLevel3(0.0625f);
			break;
		default:
			_cardsModel.renderEmptySlot(0.0625f);
		}
	}
}