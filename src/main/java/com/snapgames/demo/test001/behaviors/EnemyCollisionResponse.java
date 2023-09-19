package com.snapgames.demo.test001.behaviors;

import com.snapgames.core.entity.Entity;
import com.snapgames.core.physic.CollisionResponseBehavior;
import com.snapgames.core.physic.Vector2D;

public class EnemyCollisionResponse implements CollisionResponseBehavior {
    @Override
    public void response(Entity o1, Entity o2, Vector2D normal) {
        if(o2.getName().equals("player")) {
            double nrj = (double) o1.getAttribute("energy", 100.0);
            nrj -= 1;
            o1.addAttribute("energy", nrj);

            double energy = (double) o2.getAttribute("energy", 0.0);
            o2.addAttribute("energy", energy - 0.1);

            if (nrj <= 0) {
                o1.setActive(false);
                o2.addAttribute("score", (int) o2.getAttribute("score", 0) + 10);
            }
        }
    }
}
