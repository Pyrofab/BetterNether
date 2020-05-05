package paulevs.betternether.blocks;

import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.entity.EntityContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.WorldView;
import paulevs.betternether.registry.BlocksRegistry;

public class BlockPottedPlant extends BlockBaseNotFull
{
	public static final EnumProperty<PottedPlantShape> PLANT = EnumProperty.of("plant", PottedPlantShape.class);
	
	public BlockPottedPlant()
	{
		super(FabricBlockSettings.of(Material.PLANT)
				.materialColor(MaterialColor.BLACK)
				.sounds(BlockSoundGroup.CROP)
				.nonOpaque()
				.noCollision()
				.breakInstantly());
		this.setDropItself(false);
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		this.setDefaultState(getStateManager().getDefaultState().with(PLANT, PottedPlantShape.AGAVE));
	}

	@Override
	public int getLuminance(BlockState state)
	{
		return state.get(PLANT) == PottedPlantShape.WILLOW ? 12 : 0;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext ePos)
	{
		Block block = state.get(PLANT).getBlock();
		Vec3d vec3d = block.getDefaultState().getOffsetPos(view, pos);
		return block.getOutlineShape(block.getDefaultState(), view, pos, ePos).offset(-vec3d.x, -0.5 - vec3d.y, -vec3d.z);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager)
	{
		stateManager.add(PLANT);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		return world.getBlockState(pos.down()).getBlock() instanceof BlockBNPot;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos)
	{
		if (!canPlaceAt(state, world, pos))
			return Blocks.AIR.getDefaultState();
		else
			return state;
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder)
	{
		Block block = state.get(PLANT).getBlock();
		return Collections.singletonList(new ItemStack(block.asItem()));
	}

	public static BlockState getPlant(Item item)
	{
		for (PottedPlantShape shape: PottedPlantShape.values())
		{
			if (shape.getItem().equals(item))
				return BlocksRegistry.POTTED_PLANT.getDefaultState().with(PLANT, shape);
		}
		return null;
	}
	
	public static enum PottedPlantShape implements StringIdentifiable
	{
		AGAVE(BlocksRegistry.AGAVE),
		BARREL_CACTUS(BlocksRegistry.BARREL_CACTUS),
		BLACK_APPLE(BlocksRegistry.BLACK_APPLE_SEED),
		BLACK_BUSH(BlocksRegistry.BLACK_BUSH),
		EGG_PLANT(BlocksRegistry.EGG_PLANT),
		INK_BUSH(BlocksRegistry.INK_BUSH_SEED),
		REEDS(BlocksRegistry.NETHER_REED),
		NETHER_CACTUS(BlocksRegistry.NETHER_CACTUS),
		NETHER_GRASS(BlocksRegistry.NETHER_GRASS),
		ORANGE_MUSHROOM(BlocksRegistry.ORANGE_MUSHROOM),
		RED_MOLD(BlocksRegistry.RED_MOLD),
		GRAY_MOLD(BlocksRegistry.GRAY_MOLD),
		MAGMA_FLOWER(BlocksRegistry.MAGMA_FLOWER),
		NETHER_WART(BlocksRegistry.WART_SEED),
		WILLOW(BlocksRegistry.WILLOW_SAPLING),
		SMOKER(BlocksRegistry.SMOKER),
		WART(Blocks.NETHER_WART);
		
		private final Block block;

		private PottedPlantShape(Block block)
		{
			this.block = block;
		}
		
		@Override
		public String asString()
		{
			return this.toString().toLowerCase();
		}
		
		public Item getItem()
		{
			return block.asItem();
		}
		
		public Block getBlock()
		{
			return block;
		}
	}
}
