package com.thekillerbunny.goofyplugin;

import com.thekillerbunny.goofyplugin.lua.GoofyAPI;
import com.mojang.brigadier.arguments.*;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.*;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.*;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.gametest.framework.TestClassNameArgument;
import net.minecraft.gametest.framework.TestFunctionArgument;
import net.minecraft.world.scores.Team;
import org.luaj.vm2.LuaFunction;

/**
 * This enum specifies any valid argument for a Brigadier node created with {@link GoofyAPI#newNode}.
 *
 * <p>
 * Currently, most of Minecraft's argument types aren't currently exposed:
 * <ul>
 * <li> Most argument types make use of a non-Lua-safe value, and will need coercion.
 * <li> Some argument types require a {@link CommandBuildContext}, which I'm currently unsure how to make.
 * <li> {@link ResourceArgument} (and its brethren: {@link ResourceKeyArgument}, {@link ResourceLocationArgument}, {@link ResourceOrTagArgument}, and {@link ResourceOrTagKeyArgument}) require specifying a registry, which, like valid ranges, is currently not possible.
 * </ul>
 *
 * <p>
 * Additionally, there are some current flaws with exposed argument types:
 * <ul>
 * <li> Some argument types, such as {@link CommandArgumentType#DOUBLE}, {@link CommandArgumentType#FLOAT}, {@link CommandArgumentType#INTEGER}, and {@link CommandArgumentType#LONG}, accept a range for valid values, but it isn't yet possible to specify a range.
 * </ul>
 */
public enum CommandArgumentType {
    // region com.mojang.brigadier.arguments
    BOOL(BoolArgumentType.bool()),
    DOUBLE(DoubleArgumentType.doubleArg()), // ranged
    FLOAT(FloatArgumentType.floatArg()), // ranged
    INTEGER(IntegerArgumentType.integer()), // ranged
    LONG(LongArgumentType.longArg()), // ranged
    STRING(StringArgumentType.string()),
    GREEDY_STRING(StringArgumentType.greedyString()),
    WORD(StringArgumentType.word()),
    // endregion
    // region net.minecraft.commands.arguments

    //ANGLE(AngleArgument.angle()),
    //COLOR(ColorArgument.color()),
    //TEXT(ComponentArgument.textComponent()),
    //COMPOUND(CompoundTagArgument.compoundTag())
    //DIMENSION(DimensionArgument.dimension()),
    //ANCHOR(EntityAnchorArgument.anchor()),
    //ENTITY(EntityArgument.?)
    //GAMEMODE(GameModeArgument.gameMode()),
    //PROFILE(GameProfileArgument.gameProfile()),
    //HEIGHTMAP(HeightmapTypeArgument.heightmap()),
    //MESSAGE(MessageArgument.message()),
    //NBT_PATH(NbtPathArgument.nbtPath()),
    //NBT_TAG(NbtTagArgument.nbtTag()),
    //OBJECTIVE(ObjectiveArgument.objective()),
    //CRITERIA(ObjectiveCriteriaArgument.criteria()),
    //OPERATION(OperationArgument.operation()),
    //PARTICLE(ParticleArgument.particle(?))
    //RANGE(RangeArgument.floatRange()),
    //INT_RANGE(RangeArgument.intRange()),
    SCOREBOARD_SLOT(ScoreboardSlotArgument.displaySlot()),
    SCORE_HOLDER(ScoreHolderArgument.scoreHolder()),
    SCORE_HOLDERS(ScoreHolderArgument.scoreHolders()),
    SLOT(SlotArgument.slot()),
    //TEAM(TeamArgument.team()),
    //TEMPLATE_MIRROR(TemplateMirrorArgument.templateMirror()),
    //TEMPLATE_ROTATION(TemplateRotationArgument.templateRotation()),
    TIME(TimeArgument.time()), // ranged
    //UUID(UuidArgument.uuid()),
    // endregion
    // region net.minecraft.commands.arguments.blocks

    //BLOCK_PREDICATE(BlockPredicateArgument.blockPredicate(?)),
    //BLOCK_STATE(BlockStateArgument.block(?)),
    // endregion
    // region net.minecraft.commands.arguments.coordinates

    //BLOCK_POS(BlockPosArgument.blockPos()),
    //COLUMN_POS(ColumnPosArgument.columnPos()),
    //ROTATION(RotationArgument.rotation()),
    //SWIZZLE(SwizzleArgument.swizzle()),
    VECTOR2(Vec2Argument.vec2()),
    VECTOR2_CENTERED(Vec2Argument.vec2(true)),
    VECTOR3(Vec3Argument.vec3()),
    VECTOR3_CENTERED(Vec3Argument.vec3(true)),
    // endregion
    // region net.minecraft.commands.arguments.item

    //FUNCTION(FunctionArgument.functions()),
    //ITEM(ItemArgument.item(?)),
    //ITEM_PREDICATE(ItemPredicateArgument.itemPredicate(?))
    // endregion
    // region net.minecraft.gametest.framework

    TEST_CLASS(TestClassNameArgument.testClassName()),
    //TEST_FUNCTION(TestFunctionArgument.testFunctionArgument()),
    // endregion
    ;

    public final ArgumentType<?> argumentType;

    CommandArgumentType(ArgumentType<?> argumentType) {
        this.argumentType = argumentType;
    }
}
