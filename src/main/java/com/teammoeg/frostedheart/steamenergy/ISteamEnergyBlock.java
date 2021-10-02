/*
 * Copyright (c) 2021 TeamMoeg
 *
 * This file is part of Frosted Heart.
 *
 * Frosted Heart is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * Frosted Heart is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Frosted Heart. If not, see <https://www.gnu.org/licenses/>.
 */

package com.teammoeg.frostedheart.steamenergy;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public interface ISteamEnergyBlock {
    public default boolean canConnectFrom(IWorld world, BlockPos pos, BlockState state, Direction dir) {
    	if(world instanceof World) {
	    	TileEntity te = Utils.getExistingTileEntity((World) world, pos);
	        if (te instanceof IConnectable) {
	        	return ((IConnectable) te).canConnectAt(dir);
	        }
    	}
        return false;
    }
    public default void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        TileEntity te = Utils.getExistingTileEntity(worldIn, fromPos);
        if (te instanceof IConnectable) {
            Vector3i vec = pos.subtract(fromPos);
            Direction dir = Direction.getFacingFromVector(vec.getX(), vec.getY(), vec.getZ());
            ((IConnectable) te).connectAt(dir);
        }
    }
}
