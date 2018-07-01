/*******************************************************************************
 *     SDR Trunk 
 *     Copyright (C) 2014-2016 Dennis Sheirer
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 ******************************************************************************/
package io.github.dsheirer.module.decode.ltrstandard;

import io.github.dsheirer.controller.channel.Channel;
import io.github.dsheirer.gui.editor.Editor;
import io.github.dsheirer.gui.editor.EditorValidationException;
import io.github.dsheirer.gui.editor.ValidatingEditor;
import io.github.dsheirer.message.MessageDirection;
import io.github.dsheirer.module.decode.config.DecodeConfiguration;
import net.miginfocom.swing.MigLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LTRStandardDecoderEditor extends ValidatingEditor<Channel>
{
    private static final long serialVersionUID = 1L;
    private JComboBox<MessageDirection> mComboDirection;

    public LTRStandardDecoderEditor()
    {
        init();
    }

    private void init()
    {
        setLayout(new MigLayout("insets 0 0 0 0,wrap 4", "[right][grow,fill][right][grow,fill]", ""));

        mComboDirection = new JComboBox<MessageDirection>();
        mComboDirection.setModel(new DefaultComboBoxModel<MessageDirection>(MessageDirection.values()));
        mComboDirection.setSelectedItem(MessageDirection.OSW);
        mComboDirection.setEnabled(false);
        mComboDirection.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setModified(true);
            }
        });

        add(new JLabel("Format:"));
        add(mComboDirection);
    }

    @Override
    public void validate(Editor<Channel> editor) throws EditorValidationException
    {
    }

    @Override
    public void setItem(Channel item)
    {
        super.setItem(item);

        if(hasItem())
        {
            mComboDirection.setEnabled(true);

            DecodeConfiguration config = getItem().getDecodeConfiguration();

            if(config instanceof DecodeConfigLTRStandard)
            {
                DecodeConfigLTRStandard ltr = (DecodeConfigLTRStandard)config;
                mComboDirection.setSelectedItem(ltr.getMessageDirection());
                setModified(false);
            }
            else
            {
                mComboDirection.setSelectedItem(MessageDirection.OSW);
                setModified(true);
            }
        }
        else
        {
            mComboDirection.setEnabled(false);
            setModified(false);
        }
    }

    @Override
    public void save()
    {
        if(hasItem() && isModified())
        {
            DecodeConfigLTRStandard ltr = new DecodeConfigLTRStandard();
            ltr.setMessageDirection((MessageDirection)mComboDirection.getSelectedItem());
            getItem().setDecodeConfiguration(ltr);
        }

        setModified(false);
    }
}
