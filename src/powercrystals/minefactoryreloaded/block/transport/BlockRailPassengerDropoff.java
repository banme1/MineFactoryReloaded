package powercrystals.minefactoryreloaded.block.transport;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.setup.MFRConfig;

public class BlockRailPassengerDropoff extends BlockFactoryRail
{
	public BlockRailPassengerDropoff()
	{
		super(true, false);
		setBlockName("mfr.rail.passenger.dropoff");
	}

	@Override
	public void onMinecartPass(World world, EntityMinecart minecart, int x, int y, int z)
	{
		if (world.isRemote)
			return;

		Class<? extends EntityLivingBase> target = isPowered(world, x, y, z) ? EntityLiving.class : EntityPlayer.class;
		if (!target.isInstance(minecart.riddenByEntity))
			return;

		Entity player = minecart.riddenByEntity;
		AxisAlignedBB dropCoords = findSpaceForPlayer(player, x, y, z, world);
		if (dropCoords == null)
			return;

		player.mountEntity(null);
		MineFactoryReloadedCore.proxy.movePlayerToCoordinates((EntityLivingBase)player,
				dropCoords.minX + (dropCoords.maxX - dropCoords.minX) / 2,
				dropCoords.minY,
				dropCoords.minZ + (dropCoords.maxZ - dropCoords.minZ) / 2);
	}

	private AxisAlignedBB findSpaceForPlayer(Entity entity, int x, int y, int z, World world)
	{
		AxisAlignedBB bb = entity.boundingBox.getOffsetBoundingBox((Math.floor(entity.posX) - entity.posX) / 2,
				(Math.floor(entity.posY) - entity.posY) / 2, (Math.floor(entity.posZ) - entity.posZ) / 2);
		bb.offset((int)bb.minX - bb.minX, (int)bb.minY - bb.minY, (int)bb.minZ - bb.minZ);
		bb.offset(x - bb.minX, 0, z - bb.minZ);
		int searchX = MFRConfig.passengerRailSearchMaxHorizontal.getInt();
		int searchY = MFRConfig.passengerRailSearchMaxVertical.getInt();

		bb.offset(0.25, -searchY + 0.01, 0.25);
		for (int offsetY = -searchY; offsetY <= searchY; offsetY++)
		{
			bb.offset(-searchX, 0, 0);
			for (int offsetX = -searchX; offsetX <= searchX; offsetX++)
			{
				bb.offset(0, 0, -searchX);
				for (int offsetZ = -searchX; offsetZ <= searchX; offsetZ++)
				{
					int targetX = MathHelper.floor_double(bb.minX + (bb.maxX - bb.minX) / 2);
					int targetY = MathHelper.floor_double(bb.minY);
					int targetZ = MathHelper.floor_double(bb.minZ + (bb.maxZ - bb.minZ) / 2);

					if (world.func_147461_a(bb).isEmpty() &&
							!isBadBlockToStandIn(world, targetX, targetY, targetZ) &&
							!isBadBlockToStandOn(world, targetX, targetY - 1, targetZ))
					{
						return bb;
					}
					bb.offset(0, 0, 1);
				}
				bb.offset(1, 0, -searchX - 1);
			}
			bb.offset(-searchX - 1, 1, 0);
		}

		return null;
	}

	private boolean isBadBlockToStandOn(World world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		if (block == null || block.isAir(world, x, y, z) ||
				isBadBlockToStandIn(world, x, y, z) ||
				!block.isSideSolid(world, x, y, z, ForgeDirection.UP))
		{
			return true;
		}
		return false;
	}

	private boolean isBadBlockToStandIn(World world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		if (block != null && (block.getMaterial().isLiquid() ||
				block instanceof BlockRailBase))
		{
			return true;
		}
		return false;
	}
}
