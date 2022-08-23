package com.teammoeg.frostedheart.mixin.stone_age;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableList;
import com.teammoeg.frostedheart.research.ResearchListeners;
import com.yanny.age.stone.blocks.FlintWorkbenchTileEntity;
import com.yanny.age.stone.recipes.FlintWorkbenchRecipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.items.wrapper.RecipeWrapper;

@Mixin(FlintWorkbenchTileEntity.class)
public class FlintWorkbenchTileEntityMixin extends TileEntity {
	@Shadow(remap = false)
	private NonNullList<ItemStack> stacks;
	@Shadow(remap = false)
	private RecipeWrapper inventoryWrapper;

	public FlintWorkbenchTileEntityMixin(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	PlayerEntity pe;

	@Overwrite(remap = false)
	private List<FlintWorkbenchRecipe> findMatchingRecipes(@Nonnull ItemStack heldItemMainhand) {
		assert this.world != null;

		return findMatchingRecipes().stream()
				.filter(flintWorkbenchRecipe -> flintWorkbenchRecipe.testTool(heldItemMainhand)).findFirst().map(ImmutableList::of).orElseGet(ImmutableList::of);
	}

	@Overwrite(remap = false)
	private List<FlintWorkbenchRecipe> findMatchingRecipes() {
		assert this.world != null;

		if (stacks.stream().allMatch(ItemStack::isEmpty))
			return ImmutableList.of();
		List<FlintWorkbenchRecipe> ret = this.world.getRecipeManager().getRecipes(FlintWorkbenchRecipe.flint_workbench,
				inventoryWrapper, this.world);
		ret.removeIf(r -> !ResearchListeners.canUseRecipe(pe, r));

		return ret;
	}

	@Inject(at = @At("HEAD"), method = "blockActivated", remap = false)
	public void fh$blockActivated(@Nonnull PlayerEntity player, @Nonnull BlockRayTraceResult hit,
			CallbackInfoReturnable<ActionResultType> cbi) {
		pe = player;
	}
}