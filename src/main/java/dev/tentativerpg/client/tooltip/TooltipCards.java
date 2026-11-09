package dev.tentativerpg.client.tooltip;

import dev.tentativerpg.TentativeRpg;

import com.google.gson.reflect.TypeToken;
import dev.tentativerpg.tooltip.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import org.joml.Vector3f;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** Pixel-aligned cards; default Minecraft type, opaque reading area, bounded animations. */
public final class TooltipCards {
    record Fx(String effect,int accent,int variant,int frames,int fps,int columns,int width,int height,int frameWidth,int frameHeight,boolean loop) {}
    private static final Map<String,Fx> FX=load();
    private static Map<String,Fx> load() {
        try(var reader=new InputStreamReader(Objects.requireNonNull(TooltipCards.class.getResourceAsStream("/fx-manifest.json")),StandardCharsets.UTF_8)) {
            return GearData.JSON.fromJson(reader,new TypeToken<Map<String,Fx>>(){}.getType());
        } catch(java.io.IOException e){throw new IllegalStateException("Missing PixelFX manifest",e);}
    }
    private static final ResourceLocation FONT=ResourceLocation.withDefaultNamespace("default");
    private static final Vector3f ITEM_KEY_LIGHT=new Vector3f(0.25f,-0.5f,1f).normalize();
    private static final Vector3f ITEM_FILL_LIGHT=new Vector3f(-0.25f,-0.5f,-1f).normalize();
    private static String hovered="";
    private static long began=0;
    private static final Set<String> SEEN=new HashSet<>();
    public static String number(double n){return n==Math.rint(n)?Long.toString((long)n):String.format(Locale.ROOT,"%.1f",n);}
    private static Component text(String value){return Component.literal(value).withStyle(s->s.withFont(FONT));}
    public static void render(GuiGraphics g,ItemStack stack,Gear gear,int mouseX,int mouseY,int screenW,int screenH) {
        Font font=Minecraft.getInstance().font;
        if(Boolean.getBoolean("tooltiprpg.capture")&&SEEN.add(gear.id()))com.mojang.logging.LogUtils.getLogger().info("Tooltip QA rendered {}",gear.id());
        boolean mythic=gear.rarity()==Gear.Rarity.MYTHIC;
        final int w=mythic?248:180;
        var titleLines=font.split(text(gear.name()),w-24);
        String rarityText=Gear.label(gear.rarity()).toUpperCase(Locale.ROOT)+"  /  "+Gear.label(gear.type());
        String classText=Gear.label(gear.role())+"  -  "+gear.element();
        var rarityLines=font.split(text(rarityText),w-24);
        var classLines=font.split(text(classText),w-24);
        int rowStep=mythic?17:12, lineStep=mythic?11:10;
        int rarityY=mythic?65:61, dividerY=mythic?82:74;
        int classY=mythic?93:81, statY=mythic?112:96;
        int resourceY=mythic?151:130;
        if(!mythic){
            rarityY=50+titleLines.size()*10+3;
            dividerY=rarityY+rarityLines.size()*10+4;
            classY=dividerY+7;
            statY=classY+classLines.size()*10+5;
            resourceY=statY+34;
        }
        var lore=font.split(text(gear.lore()),w-38);
        int resourceRows=(gear.stamina()>0?1:0)+(gear.mana()>0?1:0);
        var utility=font.split(text(gear.utility().description),w-38);
        int utilityHeight=gear.utility()==Gear.Utility.NONE?0:(mythic?19:15)+utility.size()*lineStep;
        int bodyBottom=resourceY+resourceRows*rowStep+utilityHeight;
        int h=bodyBottom+(mythic?19:12)+lore.size()*lineStep+(mythic?34:28);
        float scale=mythic?Math.min(1f,Math.min((screenW-12f)/w,(screenH-12f)/h)):1f;
        int sw=Math.round(w*scale),sh=Math.round(h*scale);
        int x=mouseX+15;if(x+sw>screenW-6)x=mouseX-sw-15;
        x=Math.max(6,Math.min(screenW-sw-6,x));
        int y=Math.max(6,Math.min(screenH-sh-6,mouseY-30));
        long now=System.nanoTime();
        String identity=gear.id()+gear.style()+gear.name();
        if(!hovered.equals(identity)){hovered=identity;began=now;}
        double t=(now-began)/1_000_000_000.0;
        Fx fx=FX.get(gear.style());
        int rarity=gear.rarity().color,accent=fx==null?rarity:fx.accent;
        g.pose().pushPose();g.pose().translate(x,y,500);g.pose().scale(scale,scale,1);
        try {
            // Gentle shadow, dark painted body, and translucent colored washes at the edges.
            g.fill(3,12,w+3,h+2,0x28000000);g.fill(1,10,w+1,h,0x58000000);
            g.fillGradient(0,8,w,h-9,mix(0xFF10131D,accent,0.07),mix(0xFF0A0D15,rarity,0.035));
            for(int i=0;i<10;i++) {
                int alpha=23-i*2;
                g.fill(i,10,w-i,11+i,alpha<<24|accent);
                g.fill(i,11,i+1,h-10,Math.max(0,16-i)<<24|accent);
                g.fill(w-1-i,11,w-i,h-10,Math.max(0,16-i)<<24|accent);
            }
            if(fx!=null) {
                // PixelFX is part of the card's interior, clipped to the panel and layered below its text.
                g.enableScissor(x+Math.round(5*scale),y+Math.round(11*scale),
                    x+Math.round((w-5)*scale),y+Math.round((h-12)*scale));
                try {
                    bodyEffects(g,gear.style(),fx,w,h,t);
                    // Subtle ink washes preserve contrast without hiding the animated scene.
                    g.fillGradient(12,45,w-12,rarityY+13,0xB90B0E16,0x950B0E16);
                    g.fill(14,classY-6,w-14,resourceY-7,0x700B0E16);
                    g.fill(14,resourceY-4,w-14,bodyBottom,0x550B0E16);
                    g.fillGradient(13,bodyBottom+11,w-13,h-29,0x8F0B0E16,0xA80B0E16);
                } finally {g.disableScissor();}
            }
            paintedEdge(g,w,h,rarity,accent,mythic);
            if(fx!=null)ornaments(g,w,h,fx,t);
            // The actual textured, extruded Minecraft item model turns once every thirty seconds.
            diamond(g,w/2,23,19,0xFF121723);
            diamondOutline(g,w/2,23,19,0xFF000000|(mythic?rarity:accent));
            if(mythic)diamondOutline(g,w/2,23,20,0xC0000000|rarity);
            diamondOutline(g,w/2,23,22,0x55000000|(mythic?rarity:accent));
            rotatingItem(g,stack,w/2,23,t);
            if(mythic){
                float titleScale=Math.min(1f,(w-36f)/Math.max(1,font.width(text(gear.name()))));
                g.pose().pushPose();g.pose().translate(w/2,50,0);g.pose().scale(titleScale,titleScale,1);
                center(g,font,gear.name(),0,0,0xFFF2ECE3);g.pose().popPose();
                center(g,font,rarityText,w/2,rarityY,0xFF000000|rarity);
                center(g,font,classText,w/2,classY,0xFFB8C4D1);
            } else {
                wrappedCenter(g,font,titleLines,w/2,50,0xFFF2ECE3);
                wrappedCenter(g,font,rarityLines,w/2,rarityY,0xFF000000|rarity);
                wrappedCenter(g,font,classLines,w/2,classY,0xFFB8C4D1);
            }
            divider(g,18,w-18,dividerY,accent);
            if(gear.type()==Gear.Kind.ARMOR){
                stat(g,font,Integer.toString(gear.armor()),"ARMOR",w/2,statY,accent);
            } else {
                stat(g,font,number(gear.damage()),"DAMAGE",w/4,statY,accent);
                g.fill(w/2,statY,w/2+1,statY+27,0x24FFFFFF);
                stat(g,font,number(gear.crit())+"%","CRIT CHANCE",3*w/4,statY,0xE5DFDB);
            }
            int cy=resourceY;
            if(gear.stamina()>0){resource(g,font,"Stamina",gear.stamina(),cy,0x8BC998,w);cy+=rowStep;}
            if(gear.mana()>0){resource(g,font,"Mana",gear.mana(),cy,0x7BAEDC,w);cy+=rowStep;}
            if(gear.utility()!=Gear.Utility.NONE){
                cy+=mythic?5:2;g.drawString(font,text("SPECIAL UTILITY"),19,cy,0xFF000000|accent,false);cy+=mythic?13:12;
                for(var line:utility){g.drawString(font,line,19,cy,0xFFCFD4DE,false);cy+=lineStep;}
                cy+=1;
            }
            divider(g,18,w-18,bodyBottom+4,rarity);
            int ly=bodyBottom+(mythic?15:11);
            for(var line:lore){g.drawString(font,line,19,ly,0xFF8E9BAE,false);ly+=lineStep;}
            // Bottom class signet interrupts the border, as in a painted manuscript seal.
            int by=h-10;
            diamond(g,w/2,by,13,0xFF10151F);diamondOutline(g,w/2,by,13,0xFF000000|rarity);
            classIcon(g,gear.role(),w/2,by,0xFF000000|rarity);
        } finally {g.pose().popPose();RenderSystem.setShaderColor(1,1,1,1);}
    }
    private static void wrappedCenter(GuiGraphics g,Font f,java.util.List<net.minecraft.util.FormattedCharSequence> lines,int x,int y,int color){
        for(var line:lines){g.drawString(f,line,x-f.width(line)/2,y,color,false);y+=10;}
    }
    private static void center(GuiGraphics g,Font f,String s,int x,int y,int color){g.drawString(f,text(s),x-f.width(text(s))/2,y,color,false);}
    private static void stat(GuiGraphics g,Font f,String value,String label,int x,int y,int color){
        g.pose().pushPose();g.pose().translate(x,y,0);g.pose().scale(1.6f,1.6f,1);center(g,f,value,0,0,0xFF000000|color);g.pose().popPose();
        center(g,f,label,x,y+22,0xFF7F8C9E);
    }
    private static void resource(GuiGraphics g,Font f,String label,int n,int y,int color,int w){
        g.drawString(f,text(label),19,y,0xFF9CAABB,false);g.drawString(f,text(Integer.toString(n)),w-19-f.width(Integer.toString(n)),y,0xFF000000|color,false);
        g.fill(88,y+3,w-47,y+5,0xFF252C39);g.fill(88,y+3,88+(w-135)*n/100,y+5,0xFF000000|color);
    }
    private static int mix(int base,int tint,double k){int r=(int)(((base>>16)&255)*(1-k)+((tint>>16)&255)*k),gg=(int)(((base>>8)&255)*(1-k)+((tint>>8)&255)*k),b=(int)((base&255)*(1-k)+(tint&255)*k);return 0xFF000000|r<<16|gg<<8|b;}
    private static void divider(GuiGraphics g,int x,int end,int y,int color){int m=(x+end)/2;g.fill(x,y,m-5,y+1,0x33000000|color);g.fill(m+6,y,end,y+1,0x33000000|color);diamondOutline(g,m,y,3,0x88000000|color);}
    private static void diamond(GuiGraphics g,int x,int y,int r,int color){for(int i=-r;i<=r;i++){int a=r-Math.abs(i);g.fill(x-a,y+i,x+a+1,y+i+1,color);}}
    private static void diamondOutline(GuiGraphics g,int x,int y,int r,int color){for(int i=-r;i<=r;i++){int a=r-Math.abs(i);g.fill(x-a,y+i,x-a+1,y+i+1,color);g.fill(x+a,y+i,x+a+1,y+i+1,color);}}
    private static void line(GuiGraphics g,int x1,int y1,int x2,int y2,int color){int dx=Math.abs(x2-x1),dy=Math.abs(y2-y1),sx=x1<x2?1:-1,sy=y1<y2?1:-1,e=dx-dy;for(int guard=0;guard<700;guard++){g.fill(x1,y1,x1+1,y1+1,color);if(x1==x2&&y1==y2)break;int e2=2*e;if(e2>-dy){e-=dy;x1+=sx;}if(e2<dx){e+=dx;y1+=sy;}}}
    private static void paintedEdge(GuiGraphics g,int w,int h,int rarity,int accent,boolean mythic){
        g.fill(2,9,w-2,10,0xB0000000|rarity);g.fill(2,h-10,w-2,h-9,0x90000000|rarity);
        g.fill(1,10,2,h-10,0x95000000|rarity);g.fill(w-2,10,w-1,h-10,0x95000000|rarity);
        for(int y=12;y<h-12;y+=3){int seed=(y*73+17)%31;int l=seed%3;g.fill(-l,y,1,y+1,((35+seed)<<24)|accent);g.fill(w-1,y+1,w+l,y+2,((40+seed)<<24)|accent);}
        for(int x=5;x<w-5;x+=3){int hash=Math.floorMod(x*1103515245+12345,37);if(hash<18)continue;int d=hash%2;g.fill(x,8-d,x+2+hash%3,9,0x21000000|accent);g.fill(x,h-9,x+2+hash%3,h-8+d,0x18000000|rarity);}
        if(mythic){
            // A bold crimson frame stays consistent even for ice, holy and arcane signatures.
            g.fill(0,8,w,10,0xFF000000|rarity);g.fill(0,h-11,w,h-9,0xFF000000|rarity);
            g.fill(0,10,2,h-11,0xFF000000|rarity);g.fill(w-2,10,w,h-11,0xFF000000|rarity);
            g.fill(3,12,w-3,13,0xA48A211C);g.fill(3,h-14,w-3,h-13,0xA48A211C);
            g.fill(3,13,4,h-14,0xA48A211C);g.fill(w-4,13,w-3,h-14,0xA48A211C);
            g.fill(2,8,22,9,0xFFFF9681);g.fill(w-22,8,w-2,9,0xFFFF9681);
        }
        // Carved acanthus scrolls replace the diagonal corner strokes.
        for(int side=0;side<2;side++)for(int bottom=0;bottom<2;bottom++){
            int ox=side==0?3:w-4,oy=bottom==0?11:h-13;
            int dx=side==0?1:-1,dy=bottom==0?1:-1;
            int gold=mix(0xFFD4B675,rarity,mythic?.35:.55);
            for(int pass=0;pass<2;pass++){
                int c=pass==0?0x90000000:gold;
                int offset=pass==0?1:0;
                for(int arm=0;arm<2;arm++){
                    int px=0,py=0;
                    for(int n=0;n<=40;n++){
                        double t=n/40.0,angle=t*Math.PI*2.1;
                        double radius=7*(1-t)+1;
                        int u=(int)Math.round(12+radius*Math.cos(angle));
                        int v=(int)Math.round(7+radius*Math.sin(angle));
                        int xx=ox+dx*(arm==0?u:v)+offset;
                        int yy=oy+dy*(arm==0?v:u)+offset;
                        if(n>0)line(g,px,py,xx,yy,c);
                        px=xx;py=yy;
                    }
                }
            }
            diamond(g,ox+dx*4,oy+dy*4,mythic?3:2,gold);
            diamond(g,ox+dx*4,oy+dy*4,1,0xFFFFE8B5);
            // Leaf-shaped filigree along the two rails.
            for(int n=0;n<3;n++){
                int u=21+n*5;
                diamond(g,ox+dx*u,oy+dy*2,2,gold);
                diamond(g,ox+dx*2,oy+dy*u,2,gold);
            }
        }
    }

    private static void bodyEffects(GuiGraphics g,String id,Fx fx,int w,int h,double t){
        // Monumental scenes span the interior with layered, independently drifting echoes.
        int size=Math.max(w+48,Math.min(360,h+20));
        int sceneHeight=size*3/4;
        int sceneY=(h-sceneHeight)/2+(int)(Math.sin(t*.25+fx.variant)*9);
        animation(g,id,fx,(w-size)/2,sceneY,size,sceneHeight,t,.82f);
        int echoWidth=w+36;
        animation(g,id,fx,-18,h-echoWidth*3/4-9,echoWidth,echoWidth*3/4,
            t+fx.frames/(double)fx.fps*.43,.42f);
        animation(g,id,fx,-12,-17,w+24,(w+24)*3/4,t*.75+2,.30f);
        // Wide pools of luminous color give the scene depth around the reading area.
        g.fillGradient(5,11,w-5,55,0x33000000|fx.accent,0x00000000);
        g.fillGradient(5,h-75,w-5,h-12,0x00000000,0x26000000|fx.accent);
    }

    private static void animation(GuiGraphics g,String id,Fx fx,int x,int y,int w,int h,double t,float alpha){
        int frame=(int)(t*fx.fps)%fx.frames;
        g.flush();
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1,1,1,alpha);
        try {
            g.blit(TentativeRpg.id("textures/fx/"+id+".png"),x,y,w,h,(frame%fx.columns)*fx.frameWidth,(frame/fx.columns)*fx.frameHeight,fx.frameWidth,fx.frameHeight,fx.width,fx.height);
        } finally {RenderSystem.setShaderColor(1,1,1,1);}
    }
    private static void rotatingItem(GuiGraphics g,ItemStack stack,int x,int y,double t){
        Minecraft mc=Minecraft.getInstance();
        var renderer=mc.getItemRenderer();
        var model=renderer.getModel(stack,mc.level,mc.player,0);
        g.flush();
        g.pose().pushPose();
        try {
            g.pose().translate(x,y,65);
            g.pose().scale(29,-29,29);
            g.pose().mulPose(Axis.XP.rotationDegrees(16));
            g.pose().mulPose(Axis.YP.rotationDegrees((float)(25+(t*12)%360)));
            // Give generated item sprites tangible edge thickness without changing world/inventory models.
            g.pose().scale(1,1,2);
            // Opposing studio lights keep both textured faces readable throughout the turn.
            RenderSystem.setShaderLights(ITEM_KEY_LIGHT,ITEM_FILL_LIGHT);
            RenderSystem.enableDepthTest();
            renderer.render(stack,ItemDisplayContext.FIXED,false,g.pose(),g.bufferSource(),
                LightTexture.FULL_BRIGHT,OverlayTexture.NO_OVERLAY,model);
            g.flush();
        } finally {
            g.pose().popPose();
            Lighting.setupFor3DItems();
            RenderSystem.setShaderColor(1,1,1,1);
        }
    }
    private static void ornaments(GuiGraphics g,int w,int h,Fx fx,double t){
        int c=0xB0000000|fx.accent;
        for(int side=0;side<2;side++)for(int n=0;n<5;n++){
            int x=side==0?4:w-5;
            double phase=t*(0.45+fx.variant*0.026)+n*1.37;
            int y=22+(int)((phase*24+n*31)%(h-48));
            int wave=(int)(Math.sin(phase*2)*3);
            switch(fx.variant){
                case 0 -> {g.fill(x+wave,y,x+wave+2,y+3,c);g.fill(x+wave,y+4,x+wave+1,y+6,0x55000000|fx.accent);}
                case 1 -> {line(g,x-3,y+4,x+3,y-4,c);line(g,x,y-5,x,y+5,c);}
                case 2,15 -> {if(Math.sin(t*5+n)>0.15){line(g,x,y-7,x+wave+3,y,c);line(g,x+wave+3,y,x-2,y+5,c);}}
                case 3 -> {diamondOutline(g,x,y,3,c);g.fill(x-2,y-2,x+2,y+2,0xFF0C1019);}
                case 4,19 -> {line(g,x-2,y+5,x+2,y-4,c);line(g,x-2,y+2,x+3,y-2,0x66000000|fx.accent);}
                case 5 -> {diamond(g,x+wave,y,2,c);g.fill(x+wave,y-4,x+wave+1,y-1,c);}
                case 6,12 -> {for(int k=0;k<14;k++){int xx=x+(int)(Math.sin(phase+k*.3)*3);g.fill(xx,y+k,xx+1,y+k+1,0x77000000|fx.accent);}}
                case 7 -> {diamond(g,x+wave,y,1,c);line(g,x+wave,y,x-wave,y+11,0x33000000|fx.accent);}
                case 8 -> {diamond(g,x+wave,y,2,c);line(g,x+wave,y,x-2,y+5,0x77000000|fx.accent);}
                case 9 -> {diamondOutline(g,x,y,4,c);diamond(g,x+wave,y+6,1,c);}
                case 10 -> {line(g,x-3,y-7,x+2,y+3,0x77000000|fx.accent);diamond(g,x+2,y+3,2,c);}
                case 11 -> {line(g,x-3,y+wave,x+3,y-wave,0x66000000|fx.accent);g.fill(x+wave,y+5,x+wave+1,y+6,c);}
                case 13 -> {diamondOutline(g,x,y,3,c);line(g,x,y,x+wave,y-5,c);}
                case 14 -> {line(g,x+wave,y+5,x-wave,y-3,c);g.fill(x-wave,y-5,x-wave+1,y-2,0x66000000|fx.accent);}
                case 16 -> {diamondOutline(g,x,y,5,c);line(g,x,y-5,x,y+5,0x66000000|fx.accent);}
                case 17 -> {line(g,x-2,y-6,x+2,y+2,c);line(g,x+2,y+2,x-2,y+7,0x66000000|fx.accent);}
                case 18 -> {line(g,x-3,y-3,x+3,y,c);line(g,x+3,y,x-3,y+3,c);}
                default -> { }
            }
        }
    }
    private static void classIcon(GuiGraphics g,Gear.Role role,int x,int y,int color){
        switch(role){
            case WARRIOR -> {line(g,x-4,y+5,x+4,y-5,color);line(g,x-4,y+1,x,y+5,color);g.fill(x+3,y-6,x+5,y-4,color);}
            case MAGE -> {diamondOutline(g,x,y,5,color);line(g,x-7,y,x+7,y,color);line(g,x,y-7,x,y+7,color);diamond(g,x,y,1,0xFFF2EEDE);}
            case ARCHER -> {line(g,x-3,y-6,x+3,y,color);line(g,x+3,y,x-3,y+6,color);line(g,x-3,y-6,x-3,y+6,color);line(g,x-5,y,x+6,y,color);line(g,x+3,y-2,x+6,y,color);}
            case PRIEST -> {g.fill(x-1,y-6,x+2,y+7,color);g.fill(x-5,y-2,x+6,y+1,color);}
        }
    }
    private TooltipCards(){}
}
