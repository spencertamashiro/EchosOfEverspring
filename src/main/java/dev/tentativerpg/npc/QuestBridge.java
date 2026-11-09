package dev.tentativerpg.npc;

import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.CustomTask;
import net.minecraft.server.level.ServerPlayer;

final class QuestBridge {
    static Cast.Line advance(ServerPlayer player, Cast.Resident resident, boolean report) {
        var file = ServerQuestFile.getInstance().orElse(null);
        if (file == null) {
            return Cast.Line.UNAVAILABLE;
        }

        TeamData team = file.getTeamData(player).orElse(null);
        return team == null ? Cast.Line.UNAVAILABLE : advance(file, team, player, resident, report);
    }

    static Cast.Line advance(ServerQuestFile file, TeamData team, ServerPlayer player,
                             Cast.Resident resident, boolean report) {
        var accept = file.getQuest(resident.questId(0));
        var target = file.getQuest(resident.questId(report ? 4 : 0));
        var task = file.getTask(resident.questId(report ? 5 : 1));
        if (accept == null || target == null || !(task instanceof CustomTask) || task.getQuest() != target
                || team.isLocked()) {
            return Cast.Line.UNAVAILABLE;
        }

        if (team.isCompleted(target)) {
            return report ? Cast.Line.REPORTED : Cast.Line.ACTIVE;
        }
        if (report) {
            if (!team.isCompleted(accept)) {
                return Cast.Line.LOCKED;
            }
            var collection = file.getQuest(resident.questId(2));
            var supplyTask = file.getTask(resident.questId(3));
            if (collection == null || !(supplyTask instanceof NpcSupplyTask supplies)
                    || supplies.getQuest() != collection) {
                return Cast.Line.UNAVAILABLE;
            }
            if (!team.isCompleted(collection)
                    && (!team.canStartTasks(collection, false) || !supplies.turnIn(team, player))) {
                return Cast.Line.WAITING;
            }
        }
        if (!team.canStartTasks(target, false)) {
            return Cast.Line.UNAVAILABLE;
        }

        team.setProgress(task, task.getMaxProgress());
        if (!team.isCompleted(target)) {
            return Cast.Line.UNAVAILABLE;
        }
        return report ? Cast.Line.FINISHED : Cast.Line.ACCEPTED;
    }

    private QuestBridge() {}
}
