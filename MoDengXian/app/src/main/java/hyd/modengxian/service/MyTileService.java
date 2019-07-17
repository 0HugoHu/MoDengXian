package hyd.modengxian.service;

import android.content.Intent;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;


public class MyTileService extends TileService{

    @Override
    public void onClick() {
        super.onClick();
        Tile tile = getQsTile();
        tile.setState(Tile.STATE_ACTIVE);
        tile.updateTile();
        Intent intent = new Intent("hyd.modengxian.action.tile");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.putExtra("source", "tile");
        startActivity(intent);
    // Called when the user click the tile
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();

        // Do something when the user removes the Tile
        }
    @Override
    public void onTileAdded() {
        super.onTileAdded();

        // Do something when the user add the Tile
        }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Tile tile = getQsTile();
        tile.setState(Tile.STATE_INACTIVE);
        tile.updateTile();
        // Called when the Tile becomes visible
        }

    @Override
    public void onStopListening() {
        super.onStopListening();
        Tile tile = getQsTile();
        tile.setState(Tile.STATE_INACTIVE);
        tile.updateTile();
        // Called when the tile is no longer visible
        }
        }